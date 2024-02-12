package amp.algebra;

import java.math.BigInteger;

public abstract class WholeNum
        extends MathNumbers {

    /**
     * This enum contains 5 settings regarding narrowing
     * The first means no narrowing, and is really rather pointless,
     * since there are non-narrowing versions of each function
     * The second means to narrow with the same sign, so an {@code IntegralNum}
     * will only be narrowed to other signed values, and the opposite for {@code NaturalNum}s.
     * The third always returns the narrowest signed type
     * The fourth always returns the narrowest unsigned type, or,
     * if used on an operation with a negative number, will give an error
     * Finally, the fifth type will return the smallest type that will hold, signed or unsigned
     */
    /*enum NarrowSettings {
        NONE,
        STRONG,
        UNSIGNED,
        SIGNED,
        SAME_SIGN_TYPE
    }*/

    /**
     * Returns the smallest signed number type that can contain this value.
     * This function uses the {@link NarrowSettings}.{@code STRONG} type;
     * the value returned will be the smallest possible that still holds the given value,
     * signed or unsigned.
     * This function assumes the input is a normal, signed long;
     * Normally, use narrowStrongValueOf if that is not the case.
     * @param val the {@link BigInteger} value to narrow and return.
     * @return the smallest {@link WholeNum} subclass that fits this value.
     */
    @NotNull
    protected static WholeNum narrowStrongPositiveValueOf(
            @Range(from = 0, to = Long.MAX_VALUE) long val
    ) {
        // See if val is too big for IntegerUnsigned
        if (val > IntegerUnsigend.MAX_VALUE)
            // Then it is LongSigned
            return new LongSigned(val);
        //else
        // See if val is too big for IntegerSigned
        if (val > Integer.MAX_VALUE)
            // Then it is IntegerUnsigned
            return new IntegerUnsigned( (int)val );
        //else
        // Val is known to be smaller than a signed int now
        int valInt = (int)val;
        // See if val is too big for ShortUnsigned
        if (valInt > ShortUnsigned.MAX_VALUE)
            // Then it is IntegerSigned
            return new IntegerSigned(valInt;
        //else
        // See if val is too big for ShortSigned
        if (valInt > Short.MAX_VALUE)
            // Then it is ShortUnsigned
            return new ShortUnsigned( (short)valInt );
        //else
        // See if val is too big for ByteUnsigned
        if (valInt > ByteUnsigned.MAX_VALUE)
            // Then it is ShortSigned
            return new ShortSigned( (short)valInt );
        //else
        // See if val is too big for ByteSigned
        if (valInt > Byte.MAX_VALUE)
            // Then it is ByteUnsigned
            return new ByteUnsigned( (byte)valInt );
        //else
        // Then it is ByteSigned
        return new ByteSigned( (byte)valInt );
    }

    /**
     * Returns the smallest signed number type that can contain this value.
     * This function uses the {@link NarrowSettings}.{@code STRONG} type;
     * the value returned will be the smallest possible that still holds the given value,
     * signed or unsigned.
     * This function assumes the input is an unsigned long;
     * Normally, use narrowStrongValueOf if that is not the case.
     * @param val the {@link BigInteger} value to narrow and return.
     * @return the smallest {@link WholeNum} subclass that fits this value.
     */
    @NotNull
    public static WholeNum narrowStrongSignedValueOf(
            long val
    ) {
        // See value is negative
        if (val < 0)
            return SignedPrimitive.narrowValueOf(val);
        //else
        // Go with other positive longs
        return narrowStrongPositiveValueOf(val);
    }

    /**
     * Returns the smallest signed number type that can contain this value.
     * This function uses the {@link NarrowSettings}.{@code STRONG} type;
     * the value returned will be the smallest possible that still holds the given value,
     * signed or unsigned.
     * This function assumes the input is an unsigned long;
     * Normally, use narrowStrongValueOf if that is not the case.
     * @param val the {@link BigInteger} value to narrow and return.
     * @return the smallest {@link WholeNum} subclass that fits this value.
     */
    @NotNull
    public static WholeNum narrowStrongUnsignedValueOf(
            long val
    ) {
        // See if val is too big for LongSigned
        // Due to 2's complement negative storage,
        // that means that signed operations would interpret it as negative.
        if (val < 0)
            return new LongUnsigned(val, null);
        //else
        // Go with other positive longs
        return narrowStrongPositiveValueOf(val);
    }

    /**
     * Returns the smallest signed number type that can contain this value.
     * This function uses the {@link NarrowSettings}.{@code STRONG} type;
     * the value returned will be the smallest possible that still holds the given value,
     * signed or unsigned.
     * @param val the {@link BigInteger} value to narrow and return.
     * @return the smallest {@link WholeNum} subclass that fits this value.
     */
    @NotNull
    public static WholeNum narrowStrongValueOf(
            @NotNull BigInteger val
    ) {
        // See if val is signed
        if (val.signum() == -1)
            // Use existing signed version if so
            return IntegralNum.narrowValueOf(val);
        //else
        // See if val is too big
        if (val.compareTo(UnsignedPrimitive.MAX_VALUE) > 0)
            // If so, just return as BigUnsignedInteger
            return new BigUnsignedInteger(val, null);
        //else
        // Get long value, since it fits
        long valLong = val.longValue();
        // Return as narrowed primitive
        return narrowStrongUnsignedValueOf(valLong);
    }

    /**
     * returns the smallest signed number type that can contain this value.
     * This function uses the {@link NarrowSettings}.{@code STRONG} type;
     * the value returned will be the smallest possible that still holds the given value,
     * signed or unsigned.
     * @param input the {@link String} with the value to parse, narrow, and return.
     * @return the smallest {@link WholeNum} subclass that fits this value.
     * @throws NumberFormatException if the input cannot be parsed
     */
    @Contract("null -> fail; _ -> new") @NotNull
    public static WholeNum narrowStrongValueOf(
            String input
    )
            throws NumberFormatException {
        return narrowStrongValueOf(input, 10);
    }

    /**
     * returns the smallest signed number type that can contain this value.
     * This function uses the {@link NarrowSettings}.{@code STRONG} type;
     * the value returned will be the smallest possible that still holds the given value,
     * signed or unsigned.
     * @param input the {@link String} with the value to parse, narrow, and return.
     * @return the smallest {@link WholeNum} subclass that fits this value.
     * @throws NumberFormatException if the input cannot be parsed
     */
    @Contract("null, _ -> fail; _, _ -> new") @NotNull
    public static WholeNum narrowStrongValueOf(
            String input,
            int radix
    )
            throws NumberFormatException {
        // Checking if the input fits in here, either signed or unsigned
        // First see if String can be unsigned long
        try {
            long inputLong = Long.parseUnsignedLong(input, radix);
            // If so, send through unsigned narrower
            // First, make wrapper array to use existing array function
            return narrowStrongUnsignedValueOf(inputLong);
        }
        catch (NumberFormatException ignored) {}
        // Perhaps it is negative then, but still fits in signed long
        try {
            long thisLong = Long.parseLong(input, radix);
            // If so, narrow as signed
            return SignedPrimitive.narrowValueOf(thisLong);
        }
        catch (NumberFormatException ignored) {}
        // Then it is too large to be narrowed
        // If the string was improperly formatted the whole time,
        // the error won't be caught this time
        BigInteger thisBI = new BigInteger(input, radix);
        // Check if negative
        if (thisBI.signum() == -1)
            // Return as Signed MathBigInteger
            return new MathBigInteger(thisBI, null);
        //else
        // Return as unsigned BigUnsignedInteger
        return new BigUnsignedInteger(thisBI, null);
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
    @NotNull
    public static WholeNum@NotNull[] narrowStrongPositiveValuesOfArray(
            long@NotNull[] values
    ) {
        // Initialize max
        long max = 0;
        // Get max value
        for (long val : values)
            max = Long.max(val, max);
        // Check if max is too big for IntegerUnsigned
        if (max > IntegerUnsigned.MAX_VALUE)
            // Then it is LongSigned
            return LongSigned.valuesOfArray(values);
        //else
        // Check if max is too big for IntegerSigned
        if (max > Integer.MAX_VALUE) {
            // Then it is IntegerUnsigned
            // Resize the array components
            int[] valuesInt = new int[values.length];
            IntegralNum l = new IntegralNum();
            for (int i = 0; i < values.length; i++)
                valuesInt[i] = (int)values[i];
            // Turn to IntegerUnsigned
            return IntegerUnsigned.valuesOfArray(valuesInt);
        }
        //else
        // We know the max fits as an unsigned int
        int maxInt = (int)max;
        // Check if max is too big for ShortUnsigned
        if (maxInt > ShortUnsigned.MAX_VALUE) {
            // Then it is IntegerSigned
            // Resize the array components
            int[] valuesInt = new int[values.length];
            for (int i = 0; i < values.length; i++)
                valuesInt[i] = (int)values[i];
            // Turn to IntegerSigned
            return IntegerSigned.valuesOfArray(valuesInt);
        }
        //else
        // Check if max is too big for ShortSigned
        if (maxInt > Short.MAX_VALUE) {
            // Then it is ShortUnsigned
            // Resize the array components
            short[] valuesShort = new short[values.length];
            for (int i = 0; i < values.length; i++)
                valuesShort[i] = (short)values[i];
            // Turn to ShortUnsigned
            return ShortUnsigned.valuesOfArray(valuesShort);
        }
        //else
        // Check if max is too big for ByteUnsigned
        if (maxInt > ByteUnsigned.MAX_VALUE) {
            // Then it is ShortSigned
            // Resize the array components
            short[] valuesShort = new short[values.length];
            for (int i = 0; i < values.length; i++)
                valuesShort[i] = (short)values[i];
            // Turn to ShortSigned
            return ShortSigned.valuesOfArray(valuesShort);
        }
        //else
        // We know the values in the array fit into some byte
        // Resize the array components
        byte[] valuesByte = new byte[values.length];
        for (int i = 0; i < values.length; i++)
            valuesByte[i] = (byte)values[i];
        // Check if max is too big for ByteSigned
        if (maxInt > Byte.MAX_VALUE)
            // Then it is ByteUnsigned
            return ByteUnsigned.valuesOfArray(valuesByte);
        //else
        // Then it is ByteSigned
        return ByteSigned.valuesOfArray(valuesByte);
    }

    /**
     * Store the given array of values in the same size, the narrowest size that fits all of them.
     * This function uses the {@link NarrowSettings}.{@code STRONG} type;
     * the value returned will be the smallest possible that still holds the given value,
     * signed or unsigned.
     * @param values the {@code long} array of values to be narrowed and stored
     * @return as array of the narrrowest {@link WholeNum} sub-instances that hold the
     *               given array.
     */
    @NotNull
    public static WholeNum@NotNull[] narrowStrongSignedValuesOfArray (
            long@NotNull[] values
    ) {
        // Check if any negative values
        for (long val : values)
            if (val < 0)
                // If so, then they all go to some signed type
                return SignedPrimitive.narrowValuesOfArray(values);
        // If nothing triggered the other return
        // Check smaller sizes
        return narrowStrongPositiveValuesOfArray(values);
    }

    /**
     * Store the given array of values in the same size, the narrowest size that fits all of them.
     * This function uses the {@link NarrowSettings}.{@code STRONG} type;
     * the value returned will be the smallest possible that still holds the given value,
     * signed or unsigned.
     * @param values the array of <em>unsigned</em> {@code long}s to be narrowed and stored
     * @return as array of the narrrowest {@link WholeNum} sub-instances that hold the
     *               given array.
     */
    @NotNull
    public static WholeNum@NotNull[] narrowStrongUnsignedValuesOfArray(
            long@NotNull[] values
    ) {
        // Check if any values here require LongUnsigned
        for (long val : values)
            // Due to 2's complement
            // Unsigned values that are negative are those larger than the signed max
            if (val < 0)
                // If so, then they all go to LongUnsigned
                return LongUnsigned.valuesOfArray(values);
            //else
            //continue
        // If nothing triggered the other return
        // Check smaller sizes
        return narrowStrongPositiveValuesOfArray(values);
    }

    /**
     * Store the given array of values in the same size, the narrowest size that fits all of them.
     * This function uses the {@link NarrowSettings}.{@code STRONG} type;
     * the value returned will be the smallest possible that still holds the given value,
     * signed or unsigned.
     * @param values the {@link BigInteger} array of values to be narrowed and stored
     * @return as array of the narrrowest {@link WholeNum} sub-instances that hold the
     *               given array.
     */
    @NotNull
    public static WholeNum@NotNull[] narrowStrongValuesOfArray(
            @NotNull BigInteger@NotNull[] values
    ) {
        // Set up max and min
        BigInteger max = BigInteger.ZERO;
        BigInteger min = BigInteger.ZERO;
        for (BigInteger val : values) {
            // See if it is new max or min
            max = max.max(val);
            min = min.min(val);
            // If the new min is negative
            if (min.signum() == -1)
                return IntegralNum.narrowValuesOfArray(values);
        }
        if (max.compareTo(UnsignedPrimitive.MAX_VALUE) > 0)
            return BigUnsignedInteger.valuesOfArray(values);
        //else
        // Need to turn array to one of unsigned longs
        long[] valuesLong = new long[values.length];
        for (int i = 0; i < values.length; i++)
            valuesLong[i] = values[i].longValue();
        return narrowStrongUnsignedValuesOfArray(valuesLong);
    }

    /**
     * Copies this {@link MathBigInteger} but with the opposite signe
     * into the smallest fitting {@link WholeNum}.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SIGNED}.
     * @return a {@link WholeNum} with the negation.
     * @throws ArithmeticException if unsigned narrowing is chosen for a negative value.
     */
    @Contract(pure = true) @NotNull
    public abstract WholeNum negate(
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException;

    /**
     * Copies this {@link WholeNum} but with an always positive sign
     * into the smallest fitting {@code NaturalNum}.
     * @param narrow the {@link NarrowSettings} type to use
     *               Defaults to {@link NarrowSettings}.{@code UNSIGNED}.
     * @return the {@link WholeNum} with the magnitude
     */
    @Contract(pure = true) @NotNull
    public abstract WholeNum abs(
            @NotNull NarrowSettings narrow
    );

    /**
     * Narrows this {@link WholeNum} number into the most narrow value it will fit,
     * according to the narrowing settings given.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return The narrowest {@link WholeNum} subclass that holds this number.
     * @throws ArithmeticException if this is negative but unsigned narrowing selected.
     */
    @Contract(pure = true) @NotNull
    public abstract WholeNum narrow(
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException;

    /**
     * Checks equality against the other {@link WholeNum}.
     * @param that, the value to check for equality against.
     * @return true if they are equal, false otherwise.
     */
    @Contract(pure = true)
    public abstract boolean equals(
            @NotNull WholeNum that
    );

    /**
     * Compare this and another {@link WholeNum}
     * and put the result in an {@code int}.
     * This function is for use with {@code boolean} logic,
     * for subtraction, there is the subtract function.
     * @param that the {@link WholeNum} to compare to.
     * @return -1 if less than, 0 if equal, and 1 if greater than.
     */
    @Contract(pure = true)
    public abstract byte compareTo(
            @NotNull WholeNum that
    );

    /**
     * Add this and another {@link WholeNum} into another {@link WholeNum}.
     * @param that the {@link WholeNum} to add.
     * @return a {@link WholeNum} with the sum.
     */
    @Contract(pure = true) @NotNull
    public abstract WholeNum add(
            @NotNull WholeNum that
    );

    /**
     * Add this and another {@link WholeNum} into another {@link WholeNum}.
     * The {@link WholeNum} will be the narrowest such value that contains the answer,
     * According to the rules of the {@link NarrowSettings}.
     * @param that the other {@link WholeNum} to add.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} with the sum.
     * @throws ArithmeticException if the answer is negative but set
     *               to be narrowed to unsigned.
     */
    @Contract(pure = true) @NotNull
    public abstract WholeNum add(
            @NotNull WholeNum that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException;

    /**
     * Subtract this by another {@link WholeNum} ,
     * putting the difference into another {@link WholeNum}.
     * @param that the {@link WholeNum} to subtract by.
     * @return a {@link WholeNum} with the difference.
     */
    @Contract(pure = true) @NotNull
    public abstract WholeNum subtract(
            @NotNull WholeNum that
    );

    /**
     * Subtract this by another {@link WholeNum},
     * putting the difference into the narrowest {@link WholeNum} that it fits in,
     * according to the rules of the {@link NarrowSettings}.
     * @param that the other {@link WholeNum} to subtract by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} with the difference.
     * @throws ArithmeticException if the answer is negative but set
     *               to be narrowed to unsigned.
     */
    @Contract(pure = true) @NotNull
    public abstract WholeNum subtract(
            @NotNull WholeNum that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException;

    /**
     * Multiply this and another {@link WholeNum} into another {@link WholeNum}.
     * @param that the {@link WholeNum} to multiply by.
     * @return a {@link WholeNum} with the product.
     */
    @Contract(pure = true) @NotNull
    public abstract WholeNum multiply(
            @NotNull WholeNum that
    );

    /**
     * Multiply this by another {@link WholeNum},
     * putting the product into the narrowest {@link WholeNum} that it fits in,
     * according to the rules of the {@link NarrowSettings}.
     * @param that the other {@link WholeNum} to multiply by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} with the product.
     * @throws ArithmeticException if the answer is negative but set
     *               to be narrowed to unsigned.
     */
    @Contract(pure = true) @NotNull
    public abstract WholeNum multiply(
            @NotNull WholeNum that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException;

    /**
     * Divide this by another {@link WholeNum},
     * putting the quotient and remainder
     * into an array of {@link WholeNum}s.
     * @param that the {@link WholeNum} to divide by.
     * @return an array of 2 {@link WholeNum} numbers;
     *              the quotient is first, followed by the remainder.
     */
    @Contract(pure = true) @NotNull
    public abstract WholeNum@NotNull[] divideAndRemainder(
            @NotNull WholeNum that
    );

    /**
     * Divide this by another {@link WholeNum},
     * putting the quotient and remainder
     * into an array of {@link WholeNum}s.
     * The actual returned value types will be dependent on the narrowing settings.
     * @param that the {@link WholeNum} to divide by.
     * @param differentNarrows a {@code boolean} determining whether the narrowing will use
     *               the smallest type that the two can both fit it,
     *               or the smallest type for each one.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return an array of 2 {@link WholeNum} numbers;
     *               the quotient is first, followed by the remainder.
     * @throws ArithmeticException if the answer is negative but set
     *               to be narrowed to unsigned.
     */
    @Contract(pure = true) @NotNull
    public abstract WholeNum@NotNull[] divideAndRemainder(
            @NotNull WholeNum that,
            boolean differentNarrows,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException;

    /**
     * Divide this by another {@link WholeNum},
     * putting the quotient into another {@link WholeNum}.
     * This version does not round, it truncates.
     * @param that the {@link WholeNum} to divide by.
     * @return a {@link WholeNum} with the quotient.
     */
    @Contract(pure = true) @NotNull
    public abstract WholeNum divide(
            @NotNull WholeNum that
    );

    /**
     * Divide this by another {@link WholeNum},
     * putting the quotient into another {@link WholeNum}.
     * This version does not round, it truncates.
     * @param that the {@link WholeNum} to divide by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return a {@link WholeNum} with the quotient.
     * @throws ArithmeticException if the answer is negative but set
     *               to be narrowed to unsigned.
     */
    @Contract(pure = true) @NotNull
    public abstract WholeNum divide(
            @NotNull WholeNum that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException;

    /**
     * Divide this by some {@link MathNumbers},
     * putting the quotient into another {@link MathNumbers}.
     * This version does not round, as it provides .
     * @param that the {@link MathNumbers} to divide by.
     * @return a {@link WholeNum} with the quotient.
     */
    //public abstract MathNumbers divideExact(MathNumbers that);

    /**
     * Divide this by another {@link WholeNum},
     * putting the quotient into another {@link WholeNum}.
     * This version does not round, it truncates.
     * @param that the {@link WholeNum} to divide by.
     * @param round the {@link RoundingMode} to use when rounding
     *              Defaults to {@link RoundingMode}.{@code HALF_EVEN}.
     * @return a {@link WholeNum} with the quotient.
     */
    //public abstract WholeNum divideRound(WholeNum that, RoundingMode round);
    /**
     * Divide this by another {@link WholeNum},
     * putting the remainder into another {@link WholeNum}.
     * @param that the {@link WholeNum} to divide by.
     * @return a {@link WholeNum} with the remainder.
     */
    @Contract(pure = true) @NotNull
    public abstract WholeNum remainder(
            @NotNull WholeNum that
    );

    /**
     * Divide this by another {@link WholeNum},
     * putting the remainder into another {@link WholeNum}.
     * @param that the {@link WholeNum} to divide by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return a {@link WholeNum} with the quotient.
     * @throws ArithmeticException if the answer is negative but set
     *               to be narrowed to unsigned.
     */
    @Contract(pure = true) @NotNull
    public abstract WholeNum remainder(
            @NotNull WholeNum that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException;

    /**
     * Raise this to the power of a positive {@code int},
     * putting the result into a {@link WholeNum}.
     * This version is for positive numbers only.
     * For negative numbers, use power.
     * @param exponent the {@code int} to raise to the power of.
     * @return a {@link WholeNum} with the result.
     * @throws ArithmeticException if exponent is negative
     */
    @Contract(pure = true) @NotNull
    public abstract WholeNum raised(int exponent)
            throws ArithmeticException;

    /**
     * Raise this to the power of another {@link WholeNum},
     * putting the result into another {@link WholeNum}.
     * @param exponent the {@link WholeNum} to raise to the power of.
     * @return a {@link WholeNum} with the result.
     */
    //public abstract WholeNum power(WholeNum exponent);
}

