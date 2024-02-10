package amp.algebra;

import java.math.BigInteger;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class IntegralNum
        extends WholeNum {

    /**
     * Returns the smallest signed number type that can contain this value.
     * This function uses the {@link NarrowSettings}.{@code Signed} type;
     * the value returned will be the smallest possible that still holds the given value,
     * signed or unsigned.
     * @param val the {@code long} value to narrow and return.
     * @return the smallest {@link SignedPrimitive} subclass that fits this value.
     */
    @Contract("_ -> new") @NotNull
    public static SignedPrimitive narrowValueOf(
            long val
    ) {
        // Check if too big for IntegerSigned
        if (val < Integer.MIN_VALUE ||
                val > Integer.MAX_VALUE)
            // Then it must be LongSigned
            return new LongSigned(val);
        //else
        // Then val can be an int
        int valInt = (int)val;
        // Check if too big for ShortSigned
        if (valInt < Short.MIN_VALUE ||
                valInt > Short.MAX_VALUE)
            // Then it must be IntegerSigned
            return new IntegerSigned(valInt);
        //else
        // Check if too big for ByteSigned
        if (valInt < Byte.MIN_VALUE ||
                valInt > Byte.MAX_VALUE)
            // Then it must be ShortSigned
            return new ShortSigned( (short)valInt );
        //else
        // Then it must be ByteSigned
        return new ByteSigned( (byte)valInt );
    }

    /**
     * returns the smallest signed number type that can contain this value
     * @param val the {@link BigInteger} with the value to narrow and return
     * @return the smallest {@link IntegralNum} subclass that fits this value
     */
    @Contract("_ -> new") @NotNull
    public static IntegralNum narrowUnsignedValueOf(
            long val
    ) {
        // See if val is outside the possible range for narrowing
        // This checks if it is over the signed max, which is normally read as negative
        if (val < 0)
            // Then it must be MBI
            return MathBigInteger.valueOfUnsigned(val);
        //else
        // It isn't important that this long is unsigned
        return narrowValueOf(val);
    }

    /**
     * returns the smallest signed number type that can contain this value
     * @param val the {@link BigInteger} with the value to narrow and return
     * @return the smallest {@link IntegralNum} subclass that fits this value
     */
    @Contract("_ -> new") @NotNull
    public static IntegralNum narrowValueOf(
            @NotNull BigInteger val
    ) {
        // See if val is outside the possible range for narrowing
        if ( val.compareTo(SignedPrimitive.MIN_VALUE) < 0 ||
                val.compareTo(SignedPrimitive.MAX_VALUE) > 0)
            // If so, just return as MBI
            return new MathBigInteger(val, null);
        //else
        // Get long value, since it fits
        long valLong = val.longValue();
        // Return as narrow primitive
        return narrowValueOf(valLong);
    }

    /**
     * returns the smallest signed number type that can contain this value
     * @param input the {@link String} with the value to parse, narrow, and return
     * @return the smallest {@link IntegralNum} subclass that fits this value
     * @throws NumberFormatException if the input cannot be parsed
     */
    @Contract("null -> fail; _ -> new") @NotNull
    public static IntegralNum narrowValueOf(
            String input
    )
            throws NumberFormatException {
        return narrowValueOf(input, 10);
    }

    /**
     * returns the smallest signed number type that can contain this value
     * @param input the {@link String} with the value to parse, narrow, and return
     * @param radix, the base of the String representation.
     * @return the smallest {@link IntegralNum} subclass that fits this value
     * @throws NumberFormatException if the input cannot be parsed
     */
    @Contract("null, _ -> fail; _, _ -> new") @NotNull
    public static IntegralNum narrowValueOf(
            String input,
            int radix
    )
            throws NumberFormatException {
        try {
            // See if fits into signed long
            long inputLong = Long.parseLong(input, radix);
            // If so, narrow and return
            return SignedPrimitive.narrowValueOf(inputLong);
        }
        catch (NumberFormatException ignored) {}
        // If it didn't fit into long, try BigInteger
        // If the error was not size-related, it will be thrown again here.
        BigInteger inputBI = new BigInteger(input, radix);
        return new MathBigInteger(inputBI, null);
    }

    /**
     * Store the given array of values in the same size, the narrowest size that fits all of them
     * This function uses the {@link NarrowSettings}.{@code STRONG} type;
     * the value returned will be the smallest possible that still holds the given value,
     * signed or unsigned.
     * @param values the {@code long} array of values to be narrowed and stored
     * @return as array of the narrrowest {@link WholeNum} sub-instances that hold the
     *               given array.
     */
    @Contract("_ -> new") @NotNull
    public static SignedPrimitive@NotNull[] narrowValuesOfArray(
            long@NotNull[] values
    ) {
        // Initialize max and min
        long max = 0;
        long min = 0;
        // Get max and min value
        for (long val : values) {
            // Update max
            max = Long.max(val, max);
            // Update min
            min = Long.min(val, min);
            // Check if new max or min are too big or low for IntegerSigned
            if (min < Integer.MIN_VALUE ||
                    max > Integer.MAX_VALUE)
                // Then it is LongSigned, and the loop is unnecessary
                return LongSigned.valuesOfArray(values);
            //else continue
        }
        // Then both the max and min fit into signed ints
        int maxInt = (int)max;
        int minInt = (int)min;
        // Check if max and min are too big or low for ShortSigned
        if (minInt < Short.MIN_VALUE ||
                maxInt > Short.MAX_VALUE) {
            // Then it is IntegerSigned
            // Resize the array components
            int[] valuesInt = new int[values.length];
            for (int i = 0; i < values.length; i++)
                valuesInt[i] = (int)values[i];
            // Turn to IntegerSigned
            return IntegerSigned.valuesOfArray(valuesInt);
        }
        //else
        // Check if max and min are too big or low for ByteSigned
        if (minInt < Byte.MIN_VALUE ||
                maxInt > Byte.MAX_VALUE) {
            // Then it is ShortSigned
            // Resize the array components
            short[] valuesShort = new short[values.length];
            for (int i = 0; i < values.length; i++)
                valuesShort[i] = (short)values[i];
            // Turn to ShortSigned
            return ShortSigned.valuesOfArray(valuesShort);
        }
        //else
        // Then it is ByteSigned
        // Resize the array components
        byte[] valuesByte = new byte[values.length];
        for (int i = 0; i < values.length; i++)
            valuesByte[i] = (byte)values[i];
        // Turn to ByteSigned
        return ByteSigned.valuesOfArray(valuesByte);
    }

    /**
     * Store the given array of values in the same size, the narrowest size that fits all of them
     * @param values the array of <em>unsigned</em> {@code long}s to be narrowed and stored
     * @return as array of the narrrowest {@link IntegralNum} sub-instances that hold the
     *               given array.
     */
    @Contract("_ -> new") @NotNull
    public static IntegralNum@NotNull[] narrowUnsignedValuesOfArray(
            long@NotNull[] values
    ) {
        // Check if any values here require BigInteger
        for (long val : values)
            // Due to 2's complement
            // Unsigned values that are negative are those larger than the signed max
            if (val < 0) {
                // If so, then turn them all to BigInteger
                BigInteger[] valuesBI = new BigInteger[values.length];
                for (int i = 0; i < values.length; i++)
                    valuesBI[i] = BigInteger.valueOf(values[i]);
                return MathBigInteger.valuesOfArray(valuesBI);
            }
            //else
            //continue
        // If nothing triggered the other return
        // Check smaller sizes
        return narrowValuesOfArray(values);
    }

    /**
     * Store the given array of values in the same size, the narrowest size that fits all of them
     * @param values the {@link BigInteger} array of values to be narrowed and stored
     * @return as array of the narrrowest {@link WholeNum} sub-instances that hold the
     *               given array.
     */
    @Contract("_ -> new") @NotNull
    public static IntegralNum@NotNull[] narrowValuesOfArray(
            @NotNull BigInteger@NotNull[] values
    ) {
        // Set up max and min
        BigInteger max = BigInteger.ZERO;
        BigInteger min = BigInteger.ZERO;
        for (BigInteger val : values) {
            // See if it is new max or min
            max = max.max(val);
            min = min.min(val);
        }
        if (min.compareTo(SignedPrimitive.MIN_VALUE) < 0 ||
                max.compareTo(SignedPrimitive.MAX_VALUE) > 0)
            return MathBigInteger.valuesOfArray(values);
        //else
        // Need to turn array to one of longs
        long[] valuesLong = new long[values.length];
        for (int i = 0; i < values.length; i++)
            valuesLong[i] = values[i].longValue();
        // Continue to look to narrow
        return narrowValuesOfArray(valuesLong);
    }

    /**
     * Copies this {@link IntegralNum} but with the opposite sign
     * into another {@link IntegralNum}.
     * @return the negated {@link IntegralNum}
     */
    @Contract(pure = true) @NotNull
    public abstract IntegralNum negate();

    /**
     * Return this value as a {@code byte}, if it can actually fit within an <em>unsigned</em> {@code byte}.
     * Or, much more likely, raise an Arithmetic Exception if it doesn't fit.
     * @return this value as a super-narrowly converted {@code byte} primitive.
     *              This value is meant to be interpreted as <b><em>unsigned</em></b>.
     * @throws ArithmeticException if the value is larger than a {@code byte}.
     */
    @Contract(pure = true)
    public abstract byte byteValueExactUnsigned()
            throws ArithmeticException;

    /**
     * Return this value as a {@code short}, if it can actually fit within
     * an <em>unsigned</em> {@code short}.
     * Or, more likely, raise an Arithmetic Exception
     * if it doesn't fit
     * @return this value as a very-narrowly converted {@code short} primitive.
     *              This value is meant to be interpreted as <b><em>unsigned</em></b>
     * @throws ArithmeticException if the value is larger than a {@code short}
     */
    @Contract(pure = true)
    public abstract short shortValueExactUnsigned()
            throws ArithmeticException;

    /**
     * Return this value as a {@code int}, if it can actually fit within
     * an <em>unsigned</em> {@code int}.
     * Or, much more likely, raise an Arithmetic Exception
     * if it doesn't fit
     * @return this value as a narrowly converted {@code int} primitive.
     *              This value is meant to be interpreted as <b><em>unsigned</em></b>
     * @throws ArithmeticException if the value is larger than a {@code int}
     */
    @Contract(pure = true)
    public abstract int intValueExactUnsigned()
            throws ArithmeticException;

    /**
     * Return this value as a {@code long}, if it can actually fit within
     * an <em>unsigned</em> {@code long}.
     * Or, much more likely, raise an Arithmetic Exception
     * if it doesn't fit
     * @return this value as a narrowly converted {@code long} primitive.
     *              This value is meant to be longerpreted as <b><em>unsigned</em></b>
     * @throws ArithmeticException if the value is larger than a {@code long}
     */
    @Contract(pure = true)
    public abstract long longValueExactUnsigned()
            throws ArithmeticException;

}
