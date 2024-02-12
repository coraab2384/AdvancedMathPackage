package amp.algebra;

import java.math.BigInteger;

public class MathBigInteger
        extends IntegerNum {
    /**
     * The actual {@link BigInteger} value
     */
    @NotNull
    private final BigInteger n;

    /**
     * Constructor for {@link BigInteger} values.
     * This is for non-public use when the input is known to not be {@code null},
     * such as an output of a function that does not output {@code null}.
     * @param val the {@link BigInteger}.
     * @param nullThis to differentiate this constructor
     */
    protected MathBigInteger(
            @NotNull BigInteger val,
            @Nullable NullType nullThis
    ) {
        n = val;
    }

    /**
     * Constructor for {@link BigInteger} values.
     * This is for non-public use when the input is known to not be {@code null},
     * such as an output of a function that does not output {@code null}.
     * @param val the {@link BigInteger}.
     */
    @Contract("null -> fail")
    public MathBigInteger(
            BigInteger val
    ) {
        if (val == null)
            throw new NullPointerException("Null value");
        n = val;
    }

    /**
     * Static factory for {@code long}s and {@link Long}s.
     * @param val the {@code long} input.
     * @return the {@link MathBigInteger} constructed from val.
     */
    @NotNull
    @Contract("_ -> new")
    public static MathBigInteger valueOf(
            long val
    ) {
        // Get value as BI
        BigInteger valBI = BigInteger.valueOf(val);
        // Return as MBI
        return new MathBigInteger(valBI, null);
    }

    /**
     * Static factory for {@code long}s and {@link Long}s.
     * This one assumes the value is inherently unsigned
     * @param val the {@code long} input.
     * @return the {@link MathBigInteger} constructed from val.
     */
    @NotNull
    @Contract("_ -> new")
    public static MathBigInteger valueOfUnsigned(
            long val
    ) {
        // Initialize answer before if
        BigInteger valBI;
        // See if val is in unsigned long territory
        if (val < 0) {
            // Get overflow of signed long
            long valOverLong = val - Long.MAX_VALUE;
            // Get that as BI
            BigInteger valOverBI = BigInteger.valueOf(valOverLong);
            // Add that to static signed long max, also as BI
            valBI = valOverBI.add(SignedPrimitive.MAX_VALUE);
        }
        else
            // Make BI normally
            valBI = BigInteger.valueOf(val);
        // Return as MBI
        return new MathBigInteger(valBI, null);
    }

    /**
     * Static factory for {@link BigInteger}s.
     * @param val the {@link BigInteger} input.
     * @return the {@link MathBigInteger} constructed from val.
     * @throws NumberFormatException if val is {@code null}.
     */
    @NotNull
    @Contract("_ -> new")
    public static MathBigInteger valueOf(
            @NotNull BigInteger val
    )
            throws NumberFormatException {
        return new MathBigInteger(val, null);
    }

    /**
     * Static factory for various {@link WholeNum}s.
     * @param val the {@link WholeNum} input.
     * @return the {@link MathBigInteger} constructed from val.
     */
    @NotNull
    public static MathBigInteger valueOf(
            @NotNull WholeNum val
    ) {
        // Check if already is MBI
        if (val instanceof MathBigInteger)
            // Recast and return
            return (MathBigInteger)val;
        //else
        // Turn val to BigInteger
        BigInteger thisBI = val.toBigInteger();
        // Return as MBI
        return new MathBigInteger(thisBI, null);
    }

    /**
     * Static factory for {@link String}s, assuming base 10.
     * @param input the {@link String} to parse.
     * @return the {@link MathBigInteger} constructed from val.
     * @throws NumberFormatException if val is {@code null}.
     */
    @NotNull
    @Contract("null -> fail; _ -> new")
    public static MathBigInteger valueOf(
            String input
    )
            throws NumberFormatException {
        return valueOf(input, 10);
    }

    /**
     * Static factory for {@link String}s, assuming base 10.
     * @param input the {@link String} to parse.
     * @param radix the {@code int} with base that the string is in.
     * @return the {@link MathBigInteger} constructed from val.
     * @throws NumberFormatException if val is {@code null} or if radix
     *              is invalid
     */
    @NotNull
    @Contract("null, _ -> fail; _, _ -> new")
    public static MathBigInteger valueOf(
            String input,
            int radix
    )
            throws NumberFormatException {
        // Get string into BigInteger
        BigInteger thisBI = new BigInteger(input, radix);
        // Stick it into a MathBigInteger
        return new MathBigInteger(thisBI, null);
    }

    /**
     * Creates an array of {@link MathBigInteger}s out of
     * the values in the input array
     * @param values the array of {@link BigInteger} values to convert
     * @return an array of {@link MathBigInteger}s.
     */
    @NotNull
    @Contract("_ -> new")
    public static MathBigInteger@NotNull[] valuesOfArray(
            @NotNull BigInteger@NotNull[] values
    ) {
        MathBigInteger[] ansA = new MathBigInteger[values.length];
        for (int i = 0; i < values.length; i++)
            ansA[i] = new MathBigInteger(values[i], null);
        return ansA;
    }

    /**
     * Check the signum of the contained {@link BigInteger} against 0.
     * See signum method of {@link BigInteger}.
     * @return true if the signum is 0, false otherwise.
     */
    @Contract(pure = true)
    public boolean isZero() {
        // Check if the signum of the contained BigInteger is 0
        return (n.signum() == 0);
    }

    /**
     * Check the  of the contained {@link BigInteger} against 1.
     * @return true if the signum is 0, false otherwise.
     */
    @Contract(pure = true)
    public boolean isOne() {
        return n.equals(BigInteger.ONE);
    }

    /**
     * Check the signum of the contained {@link BigInteger} against -1.
     * See signum method of {@link BigInteger}.
     * @return true if the signum is -1, false otherwise.
     */
    @Contract(pure = true)
    public boolean isNegative() {
        // Check if the signum of the contained BigInteger is -1
        // See signum method of BigInteger
        return (n.signum() == -1);
    }

    /**
     * Check if this value is positive
     * @return the signum if this, which is
     *             -1 if negative, 0 if 0, and 1 if greater than.
     */
    @Contract(pure = true)
    public byte signum() {
        // Return the signum of the stored MathBigInteger,
        // but cast as a byte
        return (byte)( (this.n).signum() );
    }

    /**
     * Copies this {@link MathBigInteger} but with the opposite sign
     * into another {@link MathBigInteger}.
     * @return the negated {@link MathBigInteger}
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public MathBigInteger negate() {
        // Negate the contained BigInteger
        BigInteger thisNeg = n.negate();
        // Return it
        return new MathBigInteger(thisNeg, null);
    }

    /**
     * Copies this {@link MathBigInteger} but with the opposite signe
     * into the smallest fitting {@link WholeNum}.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SIGNED}.
     * @return a {@link WholeNum} with the negation.
     * @throws ArithmeticException if unsigned narrowing is chosen for a negative value.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public WholeNum negate(
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Negate the contained BigInteger
        BigInteger thisNeg = n.negate();
        // Narrow and return
        return narrowNexus(thisNeg, narrow);
    }

    /**
     *Copies this {@link MathBigInteger} but with an always positive sign
     * into another {@link MathBigInteger}.
     * @return a {@link MathBigInteger} with the magnitude.
     */
    @NotNull
    @Contract(pure = true)
    public MathBigInteger abs() {
        // See if negative
        if (n.signum() == -1)
            // Then negate
            return this.negate();
        //else
        return this;
    }

    /**
     * Copies this {@link MathBigInteger} but with a positive sign
     * into the smallest fitting {@link WholeNum}.
     * For this function, {@link NarrowSettings}.{@code SAME_SIGN_TYPE} functions like
     * {@link NarrowSettings}.{@code UNSIGNED}, instead of {@link NarrowSettings}.{@code SIGNED} like
     * it normally does for {@link MathBigInteger}s.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code UNSIGNED}.
     * @return a {@link WholeNum} with the magnitude.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public WholeNum abs(
            @NotNull NarrowSettings narrow
    ) {
        // Get abs
        BigInteger ansBI = n.abs();
        // Switch and return based on NarrowSettings
        // Here we do not use narrowNexus because we have a difference default
        return switch (narrow) {
            case NONE -> new MathBigInteger(ansBI, null);
            case STRONG -> narrowStrongValueOf(ansBI);
            case SIGNED -> narrowValueOf(ansBI);
            default -> NaturalNum.narrowValueOf(ansBI);
        };
    }

    /**
     * Given the {@link NarrowSettings}, switch the output to return the correctly narrowed
     * type of {@link WholeNum} to contain the first {@link BigInteger} value.
     * @param val the {@link BigInteger} value to contain
     * @param narrow the type of {@link NarrowSettings} to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the narrowest {@link WholeNum} subclass member that can contain val,
     *               according to the {@link NarrowSettings}.
     * @throws ArithmeticException if unsigned narrowing is chosen for a negative value.
     */
    @NotNull
    @Contract("_, _ -> new")
    private static WholeNum narrowNexus(
            @NotNull BigInteger val,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        return switch (narrow) {
            case NONE -> new MathBigInteger(val, null);
            case STRONG -> narrowStrongValueOf(val);
            case UNSIGNED -> NaturalNum.narrowValueOf(val);
            default -> narrowValueOf(val);
        };
    }

    /**
     * Narrows this {@link MathBigInteger} into the most narrow value it will fit,
     * based on the {@link NarrowSettings} type.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return The narrowest signed {@link WholeNum} subclass that holds this number.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NotNull
    @Contract(pure = true)
    public WholeNum narrow(
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Switch based on NarrowSettings
        switch (narrow) {
            case NONE:
                // If none, then do nothing
                return this;
            case UNSIGNED:
                // Check if negative
                if (n.signum() == -1)
                    // If so, throw up
                    throw new ArithmeticException("Negative value cannot use unsigned narrowing");
                //else
                //Use NaturalNum narrow factory
                return NaturalNum.narrowValueOf(n);
            case STRONG:
                // Check if not negative
                if (n.signum() != -1) {
                    // If not negative, check if too large
                    if ( n.compareTo(UnsignedPrimitive.MAX_VALUE) > 0 )
                        // If too large, can't be narrowed, so return this
                        return this;
                    //else
                    // Get long value
                    long thisLong = n.longValue();
                    // Then is not negative but is small enough to narrow
                    // Use strong narrow factory
                    return narrowStrongUnsignedValueOf(thisLong);
                }
                //else
                //Carry on into default
            //case SIGNED, SAME_SIGN_TYPE, and negative carryover from case STRONG
            default:
                // Check if it can be narrowed or not
                if (n.compareTo(SignedPrimitive.MIN_VALUE) < 0 ||
                        n.compareTo(SignedPrimitive.MAX_VALUE) > 0)
                    // If not, return it as it is
                    return this;
                //else
                // Get long value
                long thisLong = n.longValue();
                // Use narrow value factory for signed longs
                return narrowValueOf(thisLong);
        }
    }

    /**
     * Checks equality against the other {@link MathBigInteger}.
     * @param that, the value to check for equality against.
     * @return true if they are equal, false otherwise.
     */
    @Contract(pure = true)
    public boolean equals(
            @NotNull MathBigInteger that
    ) {
        return (this.n).equals(that.n);
    }

    /**
     * Checks equality against the other {@link SignedPrimitive}.
     * @param that, the value to check for equality against.
     * @return true if they are equal, false otherwise.
     */
    @Contract(pure = true)
    public boolean equals(
            @NotNull SignedPrimitive that
    ) {
        // Check if even can be equal
        if (n.compareTo(SignedPrimitive.MIN_VALUE) < 0 ||
                n.compareTo(SignedPrimitive.MAX_VALUE) > 0)
            // This cannot fit in same type as that
            return false;
        //else
        // Then this can be narrowed to at least long
        long thisLong = (this.n).longValue();
        // Get as SignedPrimitive
        LongSigned thisLS = new LongSigned(thisLong);
        // Check equality
        return thisLS.equals(that);
    }

    /**
     * Checks equality against the other {@link UnsignedPrimitive}.
     * @param that, the value to check for equality against.
     * @return true if they are equal, false otherwise.
     */
    @Contract(pure = true)
    public boolean equals(
            @NotNull UnsignedPrimitive that
    ) {
        //Check if even can be equal
        if (n.signum() == -1 ||
        n.compareTo(UnsignedPrimitive.MAX_VALUE) > 0)
            // This cannot fit in same type of that
            return false;
        //else
        // Then this can be narrowed to unsigned long
        long thisLong = (this.n).longValue();
        // Get as UnsignedPrimitive
        LongUnsigned thisLU = new LongUnsigned(thisLong);
        // Check equality
        return thisLU.equals(that);
    }

    /**
     * Checks equality against the other {@link WholeNum}.
     * As the other various {@link WholeNum} subclasses already have specified equals methods,
     * this one is primarily for {@link BigUnsignedInteger}.
     * But it is also for unspecified other {@link WholeNum}s too.
     * @param that, the value to check for equality against.
     * @return true if they are equal, false otherwise.
     */
    @Contract(pure = true)
    public boolean equals(
            @NotNull WholeNum that
    ) {
        // Get that as BI
        BigInteger thatBI = that.toBigInteger();
        // Check equality
        return (this.n).equals(thatBI);
    }

    /**
     * Compare this and another {@link MathBigInteger}
     * and put the result in an {@code int}.
     * This function is for use with {@code boolean} logic,
     * for subtraction, there is the subtract function.
     * @param that the {@link MathBigInteger} to compare to.
     * @return the compareTo result from
     *              the {@link BigInteger} class on both fields, which is
     *              -1 if less than, 0 if equal, and 1 if greater than.
     */
    @Contract(pure = true)
    public byte compareTo(
            @NotNull MathBigInteger that
    ) {
        // Compare the two BigIntegers
        // and return the result as a byte
        return (byte)( (this.n).compareTo(that.n) );
    }

    /**
     * Compare this and a {@link SignedPrimitive}
     * and put the result in an {@code int}.
     * This function is for use with {@code boolean} logic,
     * for subtraction, there is the subtract function.
     * @param that the {@link SignedPrimitive} to compare to.
     * @return the compareTo result from the {@link BigInteger} class,
     *              which is -1 if less than, 0 if equal, and 1 if greater than.
     */
    @Contract(pure = true)
    public byte compareTo(
            @NotNull SignedPrimitive that
    ) {
        // Check if absolutely smaller
        if ( (this.n).compareTo(SignedPrimitive.MIN_VALUE) < 0 )
            // This < that
            return (byte)(-1);
        //else
        // Check if absolutely bigger
        if ( (this.n).compareTo(SignedPrimitive.MAX_VALUE) > 0 )
            // This > that
            return (byte)1;
        //else
        // Cast to long, since we know we can
        long thisLong = (this.n).longValue();
        // Get as SignedPrimitive
        SignedPrimitve thisSP = narrowValueOf(thisLong);
        // Compare and return
        return thisSP.compareTo(that);
    }

    /**
     * Compare this and a {@link UnsignedPrimitive}
     * and put the result in an {@code int}.
     * This function is for use with {@code boolean} logic,
     * for subtraction, there is the subtract function.
     * @param that the {@link UnsignedPrimitive} to compare to.
     * @return the compareTo result from the {@link BigInteger} class,
     *              which is -1 if less than, 0 if equal, and 1 if greater than.
     */
    @Contract(pure = true)
    public byte compareTo(
            @NotNull UnsignedPrimitive that
    ) {
        // Check if absolutely smaller
        if ( (this.n).signum() == -1 )
            // This is negative and intrinsically less than that
            return (byte)(-1);
        //else
        // Check if absolutely bigger
        if ( (this.n).compareTo(UnsignedPrimitive.MAX_VALUE) > 0 )
            // This > that
            return (byte)1;
        //else
        // Cast to unsigned long, since we know we can
        long thisLong = (this.n).longValue();
        // Get as UnsignedPrimitive
        UnsignedPrimitve thisUP = UnsignedPrimitive.narrowUnsignedValueOf(thisLong);
        // Compare and return
        return thisUP.compareTo(that);
    }

    /**
     * Checks to compare against the other {@link BigUnsignedInteger}.
     * But it is also for unspecified other {@link WholeNum}s too.
     * @param that, the value to check for equality against.
     * @return true if they are equal, false otherwise.
     */
    @Contract(pure = true)
    public byte compareTo(
            @NotNull WholeNum that
    ) {
        // Get that as BI
        BigInteger thatBI = that.toBigInteger();
        // Compare
        return (byte)( (this.n).compareTo(thatBI) );
    }

    /**
     * Add this and a {@link BigInteger} into a {@link WholeNum}.
     * The {@link WholeNum} will be the narrowest such value that contains the answer.
     * @param thatBI the {@link BigInteger} to add.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} with the sum.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    protected WholeNum add(
            @NotNull BigInteger thatBI,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Get the sum, as BigInteger
        BigInteger ansBI = (this.n).add(thatBI);
        // Route the sum through the narrowNexus
        return narrowNexus(ansBI, narrow);
    }

    /**
     * Add this and another {@link MathBigInteger} into another {@link MathBigInteger}.
     * @param that the other {@link MathBigInteger} to add.
     * @return a {@link MathBigInteger} with the sum.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public MathBigInteger add(
            @NotNull MathBigInteger that
    ) {
        // Get the sum
        BigInteger ansBI = (this.n).add(that.n);
        // Return as MBI
        return new MathBigInteger(ansBI, null);
    }

    /**
     * Add this and another {@link MathBigInteger},
     * putting the sum into the narrowest {@link WholeNum}.
     * @param that the other {@link MathBigInteger} to add.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} with the sum.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public WholeNum add(
            @NotNull MathBigInteger that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Pass to main add function,
        // but turning that to a BigInteger
        return this.add(that.n, narrow);
    }

    /**
     * Add this and a {@link WholeNum} into a {@link MathBigInteger}.
     * @param that the {@link WholeNum} to add.
     * @return a {@link MathBigInteger} with the sum.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public MathBigInteger add(
            @NotNull WholeNum that
    ) {
        // Set NarrowSettings to NONE and use function with narrowing
        return (MathBigInteger)( this.add(that, NarrowSettings.NONE) );
    }

    /**
     * Add this and a {@link WholeNum} into a {@link WholeNum}.
     * The {@link WholeNum} will be the narrowest such value that contains the answer.
     * @param that the other {@link WholeNum} to add.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} with the sum.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public WholeNum add(
            @NotNull WholeNum that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Get the BigInteger value of that
        BigInteger thatBI = that.toBigInteger();
        // Pass to main add function
        return this.add(thatBI, narrow);
    }

    /**
     * Subtract this by a {@link BigInteger},
     * putting the difference into a {@link WholeNum}.
     * The {@link WholeNum} will be the narrowest such value that contains the answer.
     * @param thatBI the {@link BigInteger} to subtract by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} with the difference.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    protected WholeNum subtract(
            @NotNull BigInteger thatBI,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Get the difference, as BigInteger
        BigInteger ansBI = (this.n).subtract(thatBI);
        // Route the difference through the narrowNexus
        return narrowNexus(ansBI, narrow);
    }

    /**
     * Subtract this by another {@link MathBigInteger},
     * putting the difference into another {@link MathBigInteger}.
     * @param that the other {@link MathBigInteger} to subtract by.
     * @return a {@link MathBigInteger} with the difference.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public MathBigInteger subtract(
            @NotNull MathBigInteger that
    ) {
        // Get the difference
        BigInteger ansBI = (this.n).subtract(that.n);
        // Return a new MathBigInteger made of the difference
        return new MathBigInteger(ansBI, null);
    }

    /**
     * Subtract this by another {@link MathBigInteger},
     * putting the difference into the narrowest possible {@link WholeNum}.
     * @param that the other {@link MathBigInteger} to subtract by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link IntegralNum} subclass with the difference.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public WholeNum subtract(
            @NotNull MathBigInteger that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Pass to main subtract function,
        // but turning that to a BigInteger
        return this.subtract(that.n, narrow);
    }

    /**
     * Subtract this by a {@link WholeNum}
     * and put the difference into a {@link MathBigInteger}.
     * @param that the {@link WholeNum} to subtract by.
     * @return a {@link MathBigInteger} with the difference.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public MathBigInteger subtract(
            @NotNull WholeNum that
    ) {
        // Set NarrowSettings to NONE and use function with narrowing
        return (MathBigInteger)( this.subtract(that, NarrowSettings.NONE) );
    }

    /**
     * Subtract this and a {@link WholeNum} into a {@link WholeNum}.
     * The {@link WholeNum} will be the narrowest such value that contains the answer
     * @param that the other {@link WholeNum} to subtract by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} subclass with the difference.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public WholeNum subtract(
            @NotNull WholeNum that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Get the BigInteger value of that
        BigInteger thatBI = that.toBigInteger();
        // Pass to main subtract function
        return this.subtract(thatBI, narrow);
    }

    /**
     * Multiply this by a {@link BigInteger},
     * putting the product into a {@link WholeNum}.
     * The {@link WholeNum} will be the narrowest such value that contains the answer.
     * @param thatBI the {@link BigInteger} to multiply by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} with the product.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    protected WholeNum multiply(
            @NotNull BigInteger thatBI,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Get the product, as BigInteger
        BigInteger ansBI = (this.n).multiply(thatBI);
        // Route the product through the narrowNexus
        return narrowNexus(ansBI, narrow);
    }

    /**
     * Multiply this by another {@link MathBigInteger},
     * putting the product into another {@link MathBigInteger}.
     * @param that the other {@link MathBigInteger} to multiply by.
     * @return a {@link MathBigInteger} with the product.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public MathBigInteger multiply(
            @NotNull MathBigInteger that
    ) {
        // Get the product
        BigInteger ansBI = (this.n).multiply(that.n);
        // Return a new MathBigInteger made of the product
        return new MathBigInteger(ansBI, null);
    }

    /**
     * Multiply this by another {@link MathBigInteger},
     * putting the product into the narrowest possible {@link WholeNum}.
     * @param that the other {@link MathBigInteger} to multiply by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link IntegralNum} subclass with the product.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public WholeNum multiply(
            @NotNull MathBigInteger that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Pass to main multiply function,
        // but turning that to a BigInteger
        return this.multiply(that.n, narrow);
    }

    /**
     * Multiply this by a {@link WholeNum}
     * and put the product into a {@link MathBigInteger}.
     * @param that the {@link WholeNum} to multiply by.
     * @return a {@link MathBigInteger} with the product.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public MathBigInteger multiply(
            @NotNull WholeNum that
    ) {
        // Set NarrowSettings to NONE and use function with narrowing
        return (MathBigInteger)( this.multiply(that, NarrowSettings.NONE) );
    }

    /**
     * Multiply this and a {@link WholeNum} into a {@link WholeNum}.
     * The {@link WholeNum} will be the narrowest such value that contains the answer
     * @param that the other {@link WholeNum} to multiply by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} subclass with the product.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public WholeNum multiply(
            @NotNull WholeNum that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Get the BigInteger value of that
        BigInteger thatBI = that.toBigInteger();
        // Pass to main multiply function
        return this.multiply(thatBI, narrow);
    }

    /**
     * Divide this by a {@link BigInteger},
     * putting the quotient and remainder
     * into an array of {@link WholeNum}s.
     * The actual returned value types will be dependent on the narrowing settings.
     * @param that the {@link BigInteger} to divide by.
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
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    protected WholeNum@NotNull[] divideAndRemainder(
            @NotNull BigInteger that,
            boolean differentNarrows,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Get the quotient and remainder, in a BI array
        BigInteger[] ansBIA = (this.n).divideAndRemainder(that);
        // If they are narrowed separately
        if (differentNarrows) {
            // Initialize WholeNum array to hold the answers
            WholeNum[] ansA = new WholeNum[2];
            // Narrow the quotient
            ansA[0] = narrowNexus(ansBIA[0], narrow);
            // Narrow the remainder
            ansA[1] = narrowNexus(ansBIA[1], narrow);
            // Return the array
            return ansA;
        }
        //else
        // The array will be composed of the same type
        return switch (narrow) {
            case NONE -> valuesOfArray(ansBIA);
            case STRONG -> narrowStrongValuesOfArray(ansBIA);
            case UNSIGNED -> NaturalNum.valuesOfArray(ansBIA);
            default -> narrowValuesOfArray(ansBIA);
        };
    }

    /**
     * Divide this by another {@link MathBigInteger},
     * putting the quotient and remainder
     * into an array {@link MathBigInteger}s.
     * Since division cannot grow, there is no need for an Exact version.
     * @param that the {@link MathBigInteger} to divide by.
     * @return an array of 2 {@link MathBigInteger}s;
     *              the quotient is first, followed by the remainder.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public MathBigInteger@NotNull[] divideAndRemainder(
            @NotNull MathBigInteger that
    ) {
        // Do the division
        BigInteger[] ansBIA = (this.n).divideAndRemainder(that.n);
        // Return as MBIs
        return valuesOfArray(ansBIA);
    }

    /**
     * Divide this by a {@link MathBigInteger},
     * putting the quotient and remainder
     * into an array of {@link WholeNum}s.
     * The actual returned value types will be dependent on the narrowing settings.
     * @param that the {@link MathBigInteger} to divide by.
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
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public WholeNum@NotNull[] divideAndRemainder(
            @NotNull MathBigInteger that,
            boolean differentNarrows,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Do the division and return
        return this.divideAndRemainder(that.n, differentNarrows, narrow);
    }

    /**
     * Divide this by a {@link WholeNum},
     * putting the quotient and remainder
     * into an array {@link MathBigInteger}s.
     * @param that the {@link WholeNum} to divide by.
     * @return an array of 2 {@link MathBigInteger}s;
     *              the quotient is first, followed by the remainder.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public MathBigInteger@NotNull[] divideAndRemainder(
            @NotNull WholeNum that
    ) {
        // Get the MathBigInteger value of that
        MathBigInteger thatMBI = valueOf(that);
        // Use existing function
        return this.divideAndRemainder(thatMBI);
    }

    /**
     * Divide this by a {@link WholeNum},
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
    @NotNull
    @Contract(value = "_, _, _ -> new", pure = true)
    public WholeNum@NotNull[] divideAndRemainder(
            @NotNull WholeNum that,
            boolean differentNarrows,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Get the BigInteger value of that
        BigInteger thatBI = that.toBigInteger();
        // Do the division and return
        return this.divideAndRemainder(thatBI, differentNarrows, narrow);
    }

    /**
     * Subtract this by a {@link BigInteger},
     * putting the quotient into a {@link WholeNum}.
     * This only returns the quotient!
     * The {@link WholeNum} will be the narrowest such value that contains the answer.
     * @param that the {@link BigInteger} to divide by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} with the difference.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    protected WholeNum divide(
            @NotNull BigInteger that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Get the quotient
        BigInteger ansBI = (this.n).divide(that);
        // Route the difference through the narrowNexus
        return narrowNexus(ansBI, narrow);
    }

    /**
     * Divide this by another {@link MathBigInteger},
     * putting the quotient into another {@link MathBigInteger}.
     * This only returns the quotient!
     * This version does not round, it truncates.
     * @param that the {@link MathBigInteger} to divide by.
     * @return a {@link WholeNum} with the quotient.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public MathBigInteger divide(
            @NotNull MathBigInteger that
    ) {
        // Get the quotient
        BigInteger ansBI = (this.n).divide(that.n);
        // Return a new MathBigInteger made of the quotient
        return new MathBigInteger(ansBI);
    }

    /**
     * Divide this by another {@link MathBigInteger},
     * putting the quotient into another {@link WholeNum}.
     * This only returns the quotient!
     * This version does not round, it truncates.
     * @param that the {@link MathBigInteger} to divide by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return a {@link WholeNum} with the quotient.
     * @throws ArithmeticException if the answer is negative but set
     *               to be narrowed to unsigned.
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public WholeNum divide(
            @NotNull MathBigInteger that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Pass to main divide function,
        // but turning that to a BigInteger
        return this.divide(that.n, narrow);
    }

    /**
     * Divide this by a {@link WholeNum},
     * putting the quotient into another {@link MathBigInteger}.
     * This only returns the quotient!
     * Since division cannot grow, there is no need for an Exact version.
     * @param that the {@link WholeNum} to divide by.
     * @return a {@link MathBigInteger} with the quotient.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public MathBigInteger divide(
            @NotNull WholeNum that
    ) {
        // Set NarrowSettings to NONE and use function with narrowing
        return (MathBigInteger)( this.divide(that, NarrowSettings.NONE) );
    }

    /**
     * Divide this by a {@link WholeNum},
     * putting the quotient into another {@link WholeNum}.
     * This only returns the quotient!
     * This version does not round, it truncates.
     * @param that the {@link WholeNum} to divide by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return a {@link WholeNum} with the quotient.
     * @throws ArithmeticException if the answer is negative but set
     *               to be narrowed to unsigned.
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public WholeNum divide(
            @NotNull WholeNum that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Turn that into BigInteger
        BigInteger thatBI = that.toBigInteger();
        // Pass to main divide function
        return this.divide(thatBI, narrow);
    }

    /**
     * Subtract this by a {@link BigInteger},
     * putting the remainder into a {@link WholeNum}.
     * This only returns the remainder!
     * The {@link WholeNum} will be the narrowest such value that contains the answer.
     * @param that the {@link BigInteger} to divide by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} with the difference.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    protected WholeNum remainder(
            @NotNull BigInteger that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Get the remainder
        BigInteger ansBI = (this.n).remainder(that);
        // Route the difference through the narrowNexus
        return narrowNexus(ansBI, narrow);
    }

    /**
     * Divide this by another {@link MathBigInteger},
     * putting the remainder into another {@link MathBigInteger}.
     * This only returns the remainder!
     * This version does not round, it truncates.
     * @param that the {@link MathBigInteger} to divide by.
     * @return a {@link WholeNum} with the remainder.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public MathBigInteger remainder(
            @NotNull MathBigInteger that
    ) {
        // Get the remainder
        BigInteger ansBI = (this.n).remainder(that.n);
        // Return a new MathBigInteger made of the remainder
        return new MathBigInteger(ansBI);
    }

    /**
     * Divide this by another {@link MathBigInteger},
     * putting the remainder into another {@link WholeNum}.
     * This only returns the remainder!
     * This version does not round, it truncates.
     * @param that the {@link MathBigInteger} to divide by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return a {@link WholeNum} with the remainder.
     * @throws ArithmeticException if the answer is negative but set
     *               to be narrowed to unsigned.
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public WholeNum remainder(
            @NotNull MathBigInteger that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Pass to main divide function,
        // but turning that to a BigInteger
        return this.remainder(that.n, narrow);
    }

    /**
     * Divide this by a {@link WholeNum},
     * putting the remainder into another {@link MathBigInteger}.
     * This only returns the remainder!
     * Since division cannot grow, there is no need for an Exact version.
     * @param that the {@link WholeNum} to divide by.
     * @return a {@link MathBigInteger} with the remainder.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public MathBigInteger remainder(
            @NotNull WholeNum that
    ) {
        // Set NarrowSettings to NONE and use function with narrowing
        return (MathBigInteger)( this.remainder(that, NarrowSettings.NONE) );
    }

    /**
     * Divide this by a {@link WholeNum},
     * putting the remainder into another {@link WholeNum}.
     * This only returns the remainder!
     * This version does not round, it truncates.
     * @param that the {@link WholeNum} to divide by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return a {@link WholeNum} with the remainder.
     * @throws ArithmeticException if the answer is negative but set
     *               to be narrowed to unsigned.
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public WholeNum remainder(
            @NotNull WholeNum that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Turn that into BigInteger
        BigInteger thatBI = that.toBigInteger();
        // Pass to main remainder function
        return this.remainder(thatBI, narrow);
    }

    /**
     * Square this {@link MathBigInteger}.
     * @return a {@link MathBigInteger} with the square.
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public MathBigInteger squared() {
        //Square it
        return this.multiply(this);
    }

    /**
     * Give this {@link MathBigInteger}
     * to the power of the given positive {@code int}eger exponent.
     * @param exponent the {@code int} of the exponent.
     * @return a {@link MathBigInteger} with the result.
     * @throws ArithmeticException if exponent is negative
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public MathBigInteger raised(
            @Range(from = 0, to = Integer.MAX_VALUE) int exponent
    )
            throws ArithmeticException {
        // Get the raised answer
        BigInteger ansBI = (this.n).pow(exponent);
        // Return as MBI
        return new MathBigInteger(ansBI, null);
    }

    /**
     * Return this value as a {@code byte}.
     * Information will almost definitely be lost, however.
     * @return this value as a super-narrowly converted {@code byte} primitive.
     */
    @Contract(pure = true)
    public byte byteValue() {
        // Get the byte and return
        return n.byteValue();
    }

    /**
     * Return this value as a {@code byte}, if it can actually fit within.
     * Or, much more likely, raise an Arithmetic Exception if it doesn't fit.
     * @return this value as a super-narrowly converted {@code byte} primitive.
     * @throws ArithmeticException if the value is larger than a {@code byte}.
     */
    @Contract(pure = true)
    public byte byteValueExact()
            throws ArithmeticException {
        // We'll check if the value fits
        try {
            // See if the byte fits, and return it if it does
            // If not, there's our arithmetic exception
            return n.byteValueExact();
        }
        // To throw standardized exception message
        catch (ArithmeticException ignored) {}
        // Now throw the exception
        throw new ArithmeticException("Value does not fit in byte");
    }

    /**
     * Return this value as a {@code byte}, if it can actually fit within an <em>unsigned</em> {@code byte}.
     * Or, much more likely, raise an Arithmetic Exception if it doesn't fit.
     * @return this value as a super-narrowly converted {@code byte} primitive.
     *              This value is meant to be interpreted as <b><em>unsigned</em></b>.
     * @throws ArithmeticException if the value is larger than a {@code byte}.
     */
    @Contract(pure = true)
    public byte byteValueExactUnsigned()
            throws ArithmeticException {
        // We'll check if the value fits
        try {
            // See if a short fits, and get it if it does
            // If not, there's our arithmetic exception
            int thisShort = n.shortValueExact();
            // Now check if this short can also fit into an unsigned byte
            // If it can't, then there will be an exception raised
            if (thisShort < ByteUnsigned.MAX_VALUE)
                // Then it fits, so cast and return
                return (byte)thisShort;
        }
        // To throw all our exceptions as one, with standardized message
        catch (ArithmeticException ignored) {}
        // Now throw the exception
        throw new ArithmeticException("Value does not fit in unsigned byte");
    }

    /**
     * Return this value as a {@code short}
     * Information will quite very likely be lost, however.
     * @return this value as a very-narrowly converted {@code short} primitive
     */
    @Contract(pure = true)
    public short shortValue() {
        // Get short value and return
        return n.shortValue();
    }

    /**
     * Return this value as a {@code short}, if it can actually fit within
     * Or, more likely, raise an Arithmetic Exception
     * if it doesn't fit
     * @return this value as a very-narrowly converted {@code Short} primitive
     * @throws ArithmeticException if the value is larger than a {@code short}
     */
    @Contract(pure = true)
    public short shortValueExact()
            throws ArithmeticException {
        // We'll check if the value fits
        try {
            // See if the short fits, and return it if it does
            // If not, there's our arithmetic exception
            return n.shortValueExact();
        }
        // To throw standardized exception message
        catch (ArithmeticException ignored) {}
        // Now throw the exception
        throw new ArithmeticException("Value does not fit in short");
    }

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
    public short shortValueExactUnsigned()
            throws ArithmeticException {
        // We'll check if the value fits
        try {
            // See if an int fits, and get it if it does
            // If not, there's our arithmetic exception
            int thisInt = n.intValueExact();
            // Now check if this int can also fit into an unsigned short
            // If it can't, then there will be an exception raised
            if (thisInt < ShortUnsigned.MAX_VALUE)
                // Then it fits, so cast and return
                return (short)thisInt;
        }
        // To throw all our exceptions as one, with standardized message
        catch (ArithmeticException ignored) {}
        // Now throw the exception
        throw new ArithmeticException("Value does not fit in unsigned short");
    }

    /**
     * Return this value as a {@code int}
     * Information will may very well be lost, however.
     * @return this value as a narrowly converted {@code int} primitive
     */
    @Contract(pure = true)
    public int intValue() {
        // Get the int and return
        return n.intValue();
    }

    /**
     * Return this value as a {@code int}, if it fits within
     * Or, raise an Arithmetic Exception if it doesn't
     * @return this value as a narrowly converted {@code int} primitive
     * @throws ArithmeticException if the value is larger than a {@code int}
     */
    @Contract(pure = true)
    public int intValueExact()
            throws ArithmeticException {
        // We'll check if the value fits
        try {
            // See if the int fits, and return it if it does
            // If not, there's our arithmetic exception
            return n.intValueExact();
        }
        // To throw standardized exception message
        catch (ArithmeticException ignored) {}
        // Now throw the exception
        throw new ArithmeticException("Value does not fit in int");
    }

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
    public int intValueExactUnsigned()
            throws ArithmeticException {
        // We'll check if the value fits
        try {
            // See if an long fits, and get it if it does
            // If not, there's our arithmetic exception
            long thisLong = n.longValueExact();
            // Now check if this long can also fit into an unsigned int
            // If it can't, then there will be an exception raised
            if (thisLong < IntegerUnsigned.MAX_VALUE)
                // Then it fits, so cast and return
                return (int)thisLong;
        }
        // To throw all our exceptions as one, with standardized message
        catch (ArithmeticException ignored) {}
        // Now throw the exception
        throw new ArithmeticException("Value does not fit in unsigned int");
    }

    /**
     * Return this value as a {@code long}
     * Information will might be lost, however.
     * @return this value as a narrowly converted {@code long} primitive
     */
    @Contract(pure = true)
    public long longValue() {
        // Get the long and return
        return n.longValue();
    }

    /**
     * Return this value as a {@code long}, if it fits within
     * Or, raise an Arithmetic Exception if it doesn't
     * @return this value as a narrowly converted {@code long} primitive
     * @throws ArithmeticException if the value is larger than a {@code long}
     */
    @Contract(pure = true)
    public long longValueExact()
            throws ArithmeticException {
        // We'll check if the value fits
        try {
            // See if the long fits, and return it if it does
            // If not, there's our arithmetic exception
            return n.longValueExact();
        }
        // To throw standardized exception message
        catch (ArithmeticException ignored) {}
        // Now throw the exception
        throw new ArithmeticException("Value does not fit in long");
    }

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
    public long longValueExactUnsigned()
            throws ArithmeticException {
        // If it is small enough for an unsigned long
        if (n.compareTo(UnsignedPrimitive.MAX_VALUE) < 0)
            // Get the long and return it
            // We know it will fit, so no need for the Exact version
            return n.longValue();
        //else
        // Since it doesn't fit, throw the exception
        throw new ArithmeticException("Value does not fit in unsigned long");
    }

    /**
     * Turns the contained value into a {@code float}.
     * Precision may be lost!
     * @return a {@code float} containing this value
     */
    @Contract(pure = true)
    public float floatValue() {
        return n.floatValue();
    }

    /**
     * Turns the contained value into a {@code double}.
     * Precision may be lost!
     * @return a {@code double} containing this value
     */
    @Contract(pure = true)
    public double doubleValue() {
        return n.doubleValue();
    }

    /**
     * @return this value as a {@link BigInteger}
     */
    @NotNull
    @Contract(pure = true)
    public BigInteger toBigInteger() {
        return n;
    }

    /**
     * Overrides method toString() in class {@link Object}
     * @return the value within this object as a {@link String}
     */
    @Override
    @NotNull
    @Contract(pure = true)
    public String toString() {
        return n.toString();
    }

    /**
     * @param radix the radix to use to display the string
     * @return the value within this object as a {@link String} with given radix
     */
    @NotNull
    @Contract(pure = true)
    public String toString(
            int radix
    ) {
        return n.toString(radix);
    }
}
