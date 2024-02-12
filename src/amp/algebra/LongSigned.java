package amp.algebra;

import java.math.BigInteger;
import java.util.function.IntFunction;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.signedness.qual.Unsigned;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;

import static amp.algebra.MathNumbers.Cache.*;


public class LongSigned
        extends SignedPrimitive
        implements IntFunction<LongSigned> {
    
    /**
     * Size to be generated on loading
     */
    private static final byte INITIAL_CACHE_SIZE = DEFAULT_INITIAL_CACHE_SIZE;

    /**
     * Magnitude of the highest positive number in this cache
     */
    private static int CACHE_MAGNITUDE = DEFAULT_CACHE_MAGNITUDE;
    private static IntFunction<LongSigned> access = LongSigned::new;
    
    private static final Cache<LongSigned> CACHE;
    

    /*
     * Generate initial cache
     */
    static {
        /**
         * The 0th element of each cache vector.
         */
        final LongSigned[] ZERO_MAX_MIN = {
                new LongSigned(0),
                new LongSigned(Long.MAX_VALUE),
                new LongSigned(Long.MIN_VALUE)
        };
        CACHE = new Cache<>(
                3,
                INITIAL_CACHE_SIZE,
                access,
                ZERO_MAX_MIN,
                null
        );
    }
    
    
    /**
     * The actual {@code long} value
     */
    private final long n;

    /**
     * This increases the cache magnitude, which
     * @param newMagnitude The new magnitude of the Cache.
     *                     Must be explicitly greater than the current magnitude
     * @return true if the cache was increased, and false if the existing cache
     *         already exceeds the proposed expansion.
     */
    
    public static boolean cacheMagnitudeResize(
            @Range(from = 1, to = Integer.MAX_VALUE - 1) int newMagnitude
    ) {
        int posNewMagn = CACHE.cacheResize(newMagnitude, CACHE_MAGNITUDE);
        if (posNewMagn == -1) {
            return false;
        }
        //else
        CACHE_MAGNITUDE = posNewMagn;
        return true;
    }

    /**
     *
     * @return the magnitude of the cache, that is, the magnitude of the highest value
     *         (besides the max value and min value, which is also cached)
     *         in the cache
     */
    @Contract(pure = true)
    public static int getCacheMagnitude() {
        return CACHE_MAGNITUDE;
    }

    /**
     * Constructor for {@code long} values.
     * val is assumed to be signed
     * @param val the {@code long}.
     */
    private LongSigned(
            long val
    ) {
        n = val;
    }

    /**
     * Static factory for {@code long}s and {@link Long}s.
     * Just calls the constructor.
     * @param val the {@code long} input.
     * @return the {@link LongSigned} constructed from val.
     */
    @NonNull
    public static LongSigned valueOf(
            long val
    ) {
        if (val == Long.MIN_VALUE) {
            return CACHE.getMinMaxZero( (byte) 2);
        }
        //else
        long magn = Math.abs(val);
        if (magn < CACHE_MAX) {
            return (val == 0) ?
                    CACHE.getMinMaxZero( (byte) 0 ) :
                    CACHE.queryCalcSet(
                            (int) val,
                            access
                    );
        }
        //else
        return (val == Long.MAX_VALUE) ?
                CACHE.getMinMaxZero( (byte) 1 ) :
                new LongSigned(val);
    }

    /**
     * Static factory for {@code long}s and {@link Long}s.
     * This function assumes the long is unsigned
     * @param val the {@code long} input.
     * @return the {@link LongSigned} constructed from val.
     * @throws ArithmeticException if val is too big
     */
    @NonNull
    public static LongSigned valueOfUnsigned(
            @Unsigned long val
    ) throws ArithmeticException {
        if (val < 0)
            throw new ArithmeticException("Input unsigned " +
                    long.class.getCanonicalName() + " value of " +
                    val + " does not fit within " + LongSigned.class.getCanonicalName()
            );
        //else
        return valueOf(val);
    }

    /**
     * Static factory for {@link BigInteger}s.
     * This one does not check size, so information can be lost!
     * @param val the {@link BigInteger} input.
     * @return the {@link LongSigned} constructed from val.
     * @throws ArithmeticException if val is too big
     */
    @NonNull
    @Contract("null -> fail")
    public static LongSigned valueOf(
            BigInteger val
    )
            throws ArithmeticException {
        // Try to turn val to long
        try {
            // Return as long
            return valueOf(
                    val.longValueExact()
            );
        }
        catch (ArithmeticException oldAE) {
            ArithmeticException newAE =
                    new ArithmeticException("Input " +
                            val.getClass().getCanonicalName() + " value of " +
                            val + " does not fit within " + LongSigned.class.getCanonicalName()
                    );
            newAE.initCause(oldAE);
            throw newAE;
        }
        catch (NullPointerException oldNPE) {
            NullPointerException newNPE = new NullPointerException(
                    "Null value recieved for ".concat(BigInteger.class.getCanonicalName() )
            );
            newNPE.initCause(oldNPE);
            throw newNPE;
        }
    }

    /**
     * Static factory for various other {@link WholeNum}s.
     * @param val the {@link WholeNum} input.
     * @return the {@link LongSigned} constructed from val.
     * @throws ArithmeticException if val is {@code null} or if the
     *                             {@link BigInteger} is too big to fit
     */
    @NonNull
    @Contract("null -> fail")
    public static LongSigned valueOf(
            MathNumbers val
    )
            throws ArithmeticException {
        // Check if already is LongSigned
        if (val instanceof LongSigned)
            // Recast and return
            return (LongSigned) val;
        //else
        // Try to turn val to long
        try {
            // Return as long
            return valueOf(
                    val.longValueExact()
            );
        }
        catch (ArithmeticException oldAE) {
            ArithmeticException newAE =
                    new ArithmeticException("Input " +
                            val.getClass().getCanonicalName() + " value of " +
                            val + " does not fit within " + LongSigned.class.getCanonicalName()
                    );
            newAE.initCause(oldAE);
            throw newAE;
        }
    }

    /**
     * Static factory for {@link String}s, assuming base 10.
     * @param input the {@link String} to parse.
     * @return the {@link LongSigned} constructed from val.
     * @throws NumberFormatException if input is {@code null} or
     *                               otherwise cannot be parsed.
     */
    @NonNull
    @Contract("null -> fail")
    public static LongSigned valueOf(
            String input
    )
            throws NumberFormatException {
        try {
            return valueOf(
                    Long.parseLong(input)
            );
        }
        catch (NumberFormatException oldNFE) {
            NumberFormatException newNFE =
                    new NumberFormatException("Input " +
                            String.class.getCanonicalName() + " of " +
                            input + " cannot be parsed into " + LongSigned.class.getCanonicalName()
                    );
            newNFE.initCause(oldNFE);
            throw newNFE;
        }
    }

    /**
     * Static factory for {@link String}s.
     * @param input the {@link String} to parse.
     * @param radix the base that the {@link String} is in
     * @return the {@link LongSigned} constructed from val.
     * @throws NumberFormatException if input is {@code null} or
     *                               otherwise cannot be parsed.
     */
    @NonNull
    @Contract("null, _ -> fail")
    public static LongSigned valueOf(
            String input,
            @Range(from = Character.MIN_RADIX, to = Character.MAX_RADIX) int radix
    )
            throws NumberFormatException {
        try {
            return valueOf(
                    Long.parseLong(input, radix)
            );
        }
        catch (NumberFormatException oldNFE) {
            NumberFormatException newNFE =
                    new NumberFormatException("Input " +
                            String.class.getCanonicalName() + " of " +
                            input + " with radix " + radix +
                            " cannot be parsed into " + LongSigned.class.getCanonicalName()
                    );
            newNFE.initCause(oldNFE);
            throw newNFE;
        }
    }

    /**
     * Creates an array of {@link LongSigned}s out of
     * the values in the input array
     * @param values the array of {@code long} values to convert
     * @return an array of {@link LongSigned}s.
     */
    @NonNull
    @Contract(pure = true)
    public static LongSigned@NonNull[] valuesOfArray(
            long@NonNull[] values
    ) {
        LongSigned[] ansA = new LongSigned[values.length];
        for (int i = 0; i < values.length; i++)
            ansA[i] = valueOf(values[i]);
        return ansA;
    }

    /**
     * Check the signum of the contained {@code long} against 0.
     * See signum method of {@code long}.
     * @return true if the signum is 0, false otherwise.
     */
    public boolean isZero() {
        // Check if the signum of the contained long is 0
        return (n == 0);
    }

    /**
     * Check the  of the contained {@code long} against 1.
     * @return true if the signum is 0, false otherwise.
     */
    public boolean isOne() {
        return (n == 1);
    }

    /**
     * Check if the contained {@code long} is negative
     * @return true if it is negative, false otherwise.
     */
    @Override
    public boolean isNegative() {
        return (n < 0);
    }

    /**
     * Check if this value is positive
     * @return the signum if this, which is
     *             -1 if negative, 0 if 0, and 1 if greater than.
     */
    public byte signum() {
        // Check if negative
        if (n < 0)
            return (byte)(-1);
        //else
        // Return 0 if 0, else 1
        return (n == 0) ?
                (byte)0 :
                (byte)1;
    }

    /**
     * Copies this {@link LongSigned} but with the opposite sign
     * into another {@link LongSigned}.
     * @return the negated {@link LongSigned}
     */
    @NotNull
    @Contract(pure = true)
    public IntegralNum negate() {
        return (IntegralNum)( this.negate(NarrowSettings.NONE) );
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
    @Contract(pure = true)
    public WholeNum negate(
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Check error condition
        if (n == Long.MIN_VALUE)
            return LONG_MIN_NEGATED;
        //else
        // Negate the contained long
        long thisNeg = -n;
        // Narrow and return
        return narrowNexus(thisNeg, narrow);
    }

    /**
     * Copies this {@link LongSigned} but with the opposite sign
     * into another {@link LongSigned}.
     * @return the negated {@link LongSigned}
     * @throws ArithmeticException if the negation does not fit as a signed long.
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public LongSigned negateStrict()
            throws ArithmeticException {
        // Get negation of contained long
        // If there is an exception, it is thrown here
        long thisNeg = Math.negateExact(n);
        // Return as LongSigned
        return new LongSigned(n);
    }

    /**
     *Copies this {@link LongSigned} but with an always positive sign
     * into another {@link LongSigned}, or {@link MathBigInteger}.
     * @return a {@link IntegralNum} with the magnitude.
     */
    @NotNull
    @Contract(pure = true)
    public IntegralNum abs() {
        // If negative, negate
        if (n < 0)
            return this.negate();
        //else
        return this;
    }

    /**
     *Copies this {@link LongSigned} but with a positive sign
     * into the smallest fitting {@link WholeNum}.
     * For this function, {@link NarrowSettings}.{@code SAME_SIGN_TYPE} functions like
     * {@link NarrowSettings}.{@code UNSIGNED}, instead of {@link NarrowSettings}.{@code SIGNED} like
     * it normally does for {@link LongSigned}s.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code UNSIGNED}.
     * @return a {@link WholeNum} with the magnitude.
     */
    @NotNull
    @Contract(pure = true)
    public WholeNum abs(
            @NotNull NarrowSettings narrow
    ) {
        // Check error condition
        if (n == Long.MIN_VALUE)
            return switch (narrow) {
                // If LongUnsigned not an option, then answer already exists
                case NONE, SIGNED -> LONG_MIN_NEGATED;
                // Since in this function, the long is read as unsigned
                // no negation is needed
                default -> NaturalNum.narrowValueOf(n);
            };
        //else
        // Get abs
        long ansLong = Math.abs(n);
        // Switch and return based on NarrowSettings
        // Here we do not use narrowNexus because we have a different default
        return switch (narrow) {
            case NONE -> new LongSigned(ansLong);
            case STRONG -> narrowStrongPositiveValueOf(ansLong);
            case SIGNED -> narrowValueOf(ansLong);
            default -> NaturalNum.narrowValueOf(ansLong);
        };
    }

    /**
     * Copies this {@link LongSigned} but with a positive sign
     * into another {@link LongSigned}.
     * @return the negated {@link LongSigned}
     * @throws ArithmeticException if the negation does not fit as a signed long.
     */
    @NotNull
    @Contract(pure = true)
    public LongSigned absStrict()
            throws ArithmeticException {
        // If negative
        if (n < 0)
            // Negate to make positive
            // This is where the exception would be thrown
            return this.negateStrict();
        //else
        return this;
    }

    /**
     * Given the {@link NarrowSettings}, switch the output to return the correctly narrowed
     * type of {@link WholeNum} to contain the first {@code long} value.
     * @param val the {@code long} value to contain
     * @param narrow the type of {@link NarrowSettings} to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the narrowest {@link WholeNum} subclass member that can contain val,
     *               according to the {@link NarrowSettings}.
     * @throws ArithmeticException if unsigned narrowing is chosen for a negative value.
     */
    @NotNull
    @Contract("_, _ -> new")
    private static WholeNum narrowNexus(
            long val,
            @NotNull MathObjects.Context.NarrowSettings narrow
    )
            throws ArithmeticException {
        return switch (narrow) {
            case NONE -> new LongSigned(val);
            case STRONG -> narrowStrongSignedValueOf(val);
            case UNSIGNED -> NaturalNum.narrowValueOf(val);
            default -> narrowValueOf(val);
        };
    }

    /**
     * Narrows this {@link LongSigned} into the most narrow value it will fit,
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
                if (n < 0)
                    // If so, throw up
                    throw new ArithmeticException("Negative value cannot use unsigned narrowing");
                //else
                //Use UnsignedPrimitive narrow factory
                return NaturalNum.narrowValueOf(n);
            case STRONG:
                // Check if not negative
                if (n >= 0) {
                    // If not negative, check if too large for further strong narrowing
                    if ( n > IntegerUnsigned.MAX_VALUE )
                        // If too large, can't be narrowed, so return this
                        return this;
                    //else
                    // Use strong narrow factory
                    return narrowStrongPositiveValueOf(n);
                }
                //else
                //Carry on into default
            //case SIGNED, SAME_SIGN_TYPE, and negative carryover from case STRONG
            default:
                // Check if it can be narrowed or not
                if (n < Integer.MIN_VALUE ||
                        n > Integer.MAX_VALUE )
                    // If not, return it as it is
                    return this;
                //else
                // Use narrow value factory for signed longs
                return narrowValueOf(n);
        }
    }

    /**
     * Checks equality against the other {@link LongSigned}.
     * @param that, the value to check for equality against.
     * @return true if they are equal, false otherwise.
     */
    @Contract(pure = true)
    public boolean equals(
            @NonNull SignedPrimitive that
    ) {

        return (this.n == that.longValue());
    }

    /**
     * Checks equality against the other {@link WholeNum}.
     * As the other various {@link WholeNum} subclasses already have specified equals methods,
     * this one is primarily for other {@link SignedPrimitives}s.
     * But it is also for unspecified other {@link WholeNum}s too.
     * @param that, the value to check for equality against.
     * @return true if they are equal, false otherwise.
     */
    @Contract(pure = true)
    public boolean equals(
            @NonNull WholeNum that
    ) {
        // Get that as long
        long thatLong = that.longValue();
        // Check equality
        return (this.n == thatLong);
    }

    /**
     * Checks equality against the other {@link UnsignedPrimitive}.
     * @param that, the value to check for equality against.
     * @return true if they are equal, false otherwise.
     */
    public boolean equals(NaturalNum that) {
        //Use version belonging to 'larger' type
        return that.equals(this);
    }

    private static byte compareLong(
            long a,
            long b
    ) {
        if (a < b)
            return (byte)(-1);
        //else
        return (a == b) ?
                (byte)0 :
                (byte)1;
    }

    /**
     * Compare this and another {@link LongSigned}
     * and put the result in an {@code byte}.
     * This function is for use with {@code boolean} logic,
     * for subtraction, there is the subtract function.
     * @param that the {@link LongSigned} to compare to.
     * @return -1 if less than, 0 if equal, and 1 if greater than.
     */
    public byte compareTo(LongSigned that) {
        return compareLong(this.n, that.n);
    }

    /**
     * Compare this and another {@link MathBigInteger}
     * and put the result in an {@code byte}.
     * This function is for use with {@code boolean} logic,
     * for subtraction, there is the subtract function.
     * @param that the {@link LongSigned} to compare to.
     * @return -1 if less than, 0 if equal, and 1 if greater than.
     */
    public byte compareTo(MathBigInteger that) {
        // Compare to larger type
        byte ansNeg = that.compareTo(this);
        // and negate to re-flip
        return (byte)(-ansNeg);
    }

    /**
     * Checks to compare against the other {@link SignedPrimitive}.
     * But it is also for unspecified other {@link WholeNum}s too.
     * @param that, the value to check for equality against.
     * @return -1 if less than, 0 if equal, and 1 if greater than.
     */
    public byte compareTo(@NotNull WholeNum that) {
        // Get that as BI
        long thatLong = that.longValue();
        // Compare
        return compareLong(this.n, thatLong);
    }

    /**
     * Checks equality against a {@link NaturalNum}.
     * @param that, the value to check for equality against.
     * @return -1 if less than, 0 if equal, and 1 if greater than.
     */
    public byte compareTo(NaturalNum that) {
        // Compare to larger type
        byte ansNeg = that.compareTo(this);
        // and negate to re-flip
        return (byte)(-ansNeg);
    }

    /**
     * Add this and a {@code long} into a {@link WholeNum}.
     * The {@link WholeNum} will be the narrowest such value that contains the answer.
     * @param that the {@code long} to add.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} with the sum.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NonNull
    @Contract(pure = true)
    protected WholeNum add(
            long that,
            @NonNull NarrowSettings narrow
    ) throws ArithmeticException {
        // Get the sum, as long
        long ansLong;
        try {
            ansLong = Math.addExact(n, that);
        }
        catch (ArithmeticException ignored) {
            MathBigInteger thisMBI = MathBigInteger.valueOf(this);
            BigInteger thatBI = BigInteger.valueOf(that);
            return thisMBI.add(thatBI, narrow);
        }
        // Route the sum through the narrowNexus
        return narrowNexus(ansLong, narrow);
    }

    /**
     * Add this and another {@link SignedPrimitive} into another {@link IntegralNum}.
     * @param that the other {@link SignedPrimitive} to add.
     * @return a {@link IntegralNum} with the sum.
     */
    @NonNull
    @Contract(pure = true)
    public IntegerNum add(
            @NonNull SignedPrimitive that
    ) {
        if (this.n == 0) {
            return that;
        }
        //else
        // Get long value of that
        long thatL = that.longValue();
        if (thatL == 0) {
            return this;
        }
        //else
        // Set to no narrowing, add, and return
        return (IntegerNum) ( this.add(thatL, NarrowSettings.NONE) );
    }

    /**
     * Add this and another {@link SignedPrimitive},
     * putting the sum into the narrowest {@link WholeNum}.
     * @param that the other {@link SignedPrimitive} to add.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} with the sum.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NonNull
    @Contract(pure = true)
    public WholeNum add(
            @NonNull SignedPrimitive that,
            @NonNull NarrowSettings narrow
    ) throws ArithmeticException {
        // Get long value of that
        long thatL = that.longValue();
        // Pass to main add function,
        return this.add(thatL, narrow);
    }

    /**
     * Add this and a {@link WholeNum} into a {@link LongSigned}.
     * @param that the {@link WholeNum} to add.
     * @return a {@link LongSigned} with the sum.
     */
    @NonNull
    @Contract(pure = true)
    public IntegerNum add(
            @NonNull WholeNum that
    ) {
        return (IntegerNum) ( this.add(that, NarrowSettings.NONE) );
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
    @NonNull
    @Contract(value = "_, _ -> new", pure = true)
    public WholeNum add(
            @NonNull WholeNum that,
            @NonNull NarrowSettings narrow
    ) throws ArithmeticException {
        boolean isPossibleLongUnsigned = (that instanceof UnsignedPrimitive);
        if ( (that instanceof SignedPrimitive) || isPossibleLongUnsigned) {
            // Get long value of that
            long thatL = that.longValue();
            // If this is a larger value than the negative overshoot valu
            if (isPossibleLongUnsigned) {
                if (this.n > Long.MAX_VALUE - thatL) {
                    // Then we have to use MathBigIntegers to hold the answer
                    return this.addSafe(that, narrow);
                }
                //else
                // Then the sum fits within signed long
                return narrowNexus(
                        this.n + thatL,
                        narrow
                );
            }
            //else
            return this.add(thatL, narrow);
        }
        //else
        return this.addSafe(that, narrow);
    }
    
    @NonNull
    @Contract(pure = true)
    private WholeNum addSafe(
            @NonNull WholeNum that,
            @NonNull NarrowSettings narrow
    ) {
        BigInteger thatBI = that.toBigInteger();
        MathBigInteger thisMBI = MathBigInteger.valueOf(this);
        return thisMBI.add(thatBI, narrow);
    }

    /**
     * Add this and another {@link SignedPrimitive},
     * putting the sum into another {@link LongSigned}.
     * @param that the other {@link SignedPrimitive} to add.
     * @return the {@link LongSigned} with the sum.
     * @throws ArithmeticException if the value over or underflows.
     */
    @NonNull
    @Contract(pure = true)
    public LongSigned addStrict(
            @NonNull SignedPrimitive that
    )
            throws ArithmeticException {
        // Get that long value
        long thatLong = that.longValue();
        // Get sum
        long ansLong = Math.addExact(this.n, thatLong);
        // Return as LongSigned
        return valueOf(ansLong);
    }

    /**
     * Subtract this by a {@code long},
     * putting the difference into a {@link WholeNum}.
     * The {@link WholeNum} will be the narrowest such value that contains the answer.
     * @param that the {@code long} to subtract by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} with the difference.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NonNull
    @Contract(pure = true)
    protected WholeNum subtract(
            long that,
            @NonNull NarrowSettings narrow
    ) throws ArithmeticException {
        // Get the sum, as long
        long ansLong;
        try {
            ansLong = Math.subtractExact(this.n, that);
        }
        catch (ArithmeticException ignored) {
            MathBigInteger thisMBI = MathBigInteger.valueOf(this);
            BigInteger thatBI = BigInteger.valueOf(that);
            return thisMBI.subtract(thatBI, narrow);
        }
        // Route the sum through the narrowNexus
        return narrowNexus(ansLong, narrow);
    }

    /**
     * Subtract this by another {@link SignedPrimitive},
     * putting the difference into an {@link IntegralNum}.
     * @param that the other {@link SignedPrimitive} to subtract by.
     * @return a {@link IntegralNum} with the difference.
     */
    @NonNull
    @Contract(pure = true)
    public IntegerNum subract(
            @NonNull SignedPrimitive that
    ) {
        if (this.n == 0) {
            return that;
        }
        //else
        // Get long value of that
        long thatL = that.longValue();
        // Set to no narrowing, add, and return
        return (IntegerNum) ( this.subtract(thatL, NarrowSettings.NONE) );
    }

    /**
     * Subtract this by another {@link SignedPrimitive},
     * putting the difference into the narrowest possible {@link WholeNum}.
     * @param that the other {@link SignedPrimitive} to subtract by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} subclass with the difference.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NonNull
    @Contract(pure = true)
    public WholeNum subtract(
            @NonNull SignedPrimitive that,
            @NonNull NarrowSettings narrow
    ) throws ArithmeticException {
        // Get long value of that
        long thatL = that.longValue();
        // Pass to main add function,
        return this.subtract(thatL, narrow);
    }

    /**
     * Subtract this by a {@link WholeNum}
     * and put the difference into a {@link IntegralNum}.
     * @param that the {@link WholeNum} to subtract by.
     * @return a {@link IntegralNum} with the difference.
     */
    @NonNull
    @Contract(pure = true)
    public IntegerNum subtract(
            @NonNull WholeNum that
    ) {
        return (IntegerNum) ( this.subtract(that, NarrowSettings.NONE) );
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
    @NonNull
    @Contract(pure = true)
    public WholeNum subtract(
            @NonNull WholeNum that,
            @NonNull NarrowSettings narrow
    ) throws ArithmeticException {
        boolean isPossibleLongUnsigned = (that instanceof UnsignedPrimitive);
        if ( (that instanceof SignedPrimitive) || isPossibleLongUnsigned) {
            // Get long value of that
            long thatL = that.longValue();
            // If this is a larger value than the negative overshoot value
            if (isPossibleLongUnsigned) {
                if (this.n < thatL - Long.MAX_VALUE) {
                    // Then we have to use MathBigIntegers to hold the answer
                    return this.subtractSafe(that, narrow);
                }
            }
            //else
            // Then the sum fits within signed long
            return narrowNexus(
                    this.n - thatL,
                    narrow
            );
        }
        //else
        return this.subtractSafe(that, narrow);
    }
    
    @NonNull
    @Contract(pure = true)
    private WholeNum subtractSafe(
            @NonNull WholeNum that,
            @NonNull NarrowSettings narrow
    ) {
        BigInteger thatBI = that.toBigInteger();
        MathBigInteger thisMBI = MathBigInteger.valueOf(that);
        return thisMBI.subtract(thatBI, narrow);
    }

    /**
     * Subtract this by another {@link SignedPrimitive},
     * putting the difference into another {@link LongSigned}.
     * @param that the other {@link SignedPrimitive} to subtract by.
     * @return the {@link LongSigned} with the difference.
     * @throws ArithmeticException if the value over or underflows.
     */
    @NonNull
    @Contract(pure = true)
    public LongSigned subtractStrict(
            @NonNull SignedPrimitive that
    ) throws ArithmeticException {
        // Get that long value
        long thatLong = that.longValue();
        // Get sum
        long ansLong = Math.subtractExact(this.n, thatLong);
        // Return as LongSigned
        return new LongSigned(ansLong);
    }

    /**
     * Multiply this by a {@code long},
     * putting the product into a {@link WholeNum}.
     * The {@link WholeNum} will be the narrowest such value that contains the answer.
     * @param thatBI the {@code long} to multiply by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} with the product.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NonNull
    @Contract(pure = true)
    protected WholeNum multiply(
            long that,
            @NonNull NarrowSettings narrow
    ) throws ArithmeticException {
        // Get the sum, as long
        long ansLong;
        try {
            ansLong = Math.multiplyExact(this.n, that);
        }
        catch (ArithmeticException ignored) {
            MathBigInteger thisMBI = MathBigInteger.valueOf(this);
            BigInteger thatBI = BigInteger.valueOf(that);
            return thisMBI.multiply(thatBI, narrow);
        }
        // Route the sum through the narrowNexus
        return narrowNexus(ansLong, narrow);
    }

    /**
     * Multiply this by another {@link LongSigned},
     * putting the product into another {@link LongSigned}.
     * @param that the other {@link LongSigned} to multiply by.
     * @return a {@link LongSigned} with the product.
     */
    @NonNull
    @Contract(pure = true)
    public IntegerNum multiply(
            @NonNull SignedPrimitive that
    ) {
        if (this.n == 1) {
            return that;
        }
        //else
        // Get long value of that
        long thatL = that.longValue();
        if (thatL == 1) {
            return this;
        }
        //else
        // Set to no narrowing, add, and return
        return (IntegerNum) ( this.multiply(thatL, NarrowSettings.NONE) );
    }

    /**
     * Multiply this by another {@link LongSigned},
     * putting the product into the narrowest possible {@link WholeNum}.
     * @param that the other {@link LongSigned} to multiply by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link IntegralNum} subclass with the product.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    @NonNull
    @Contract(pure = true)
    public WholeNum multiply(
            @NonNull SignedPrimitive that,
            @NonNull NarrowSettings narrow
    ) throws ArithmeticException {
        // Get long value of that
        long thatL = that.longValue();
        // Pass to main add function,
        return this.multiply(thatL, narrow);
    }

    /**
     * Multiply this by a {@link WholeNum}
     * and put the product into a {@link LongSigned}.
     * @param that the {@link WholeNum} to multiply by.
     * @return a {@link LongSigned} with the product.
     */
    @NonNull
    @Contract(pure = true)
    public IntegerNum multiply(
            @NonNull WholeNum that
    ) {
        return (IntegerNum) ( this.multiply(that, NarrowSettings.NONE) );
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
    @NonNull
    @Contract(value = "_, _ -> new", pure = true)
    public WholeNum multiply(
            @NonNull WholeNum that,
            @NonNull NarrowSettings narrow
    ) throws ArithmeticException {
        boolean isPossibleLongUnsigned = (that instanceof UnsignedPrimitive);
        if ( (that instanceof SignedPrimitive) || isPossibleLongUnsigned) {
            // Get long value of that
            long thatL = that.longValue();
            if (isPossibleLongUnsigned && (thatL < 0) ) {
                // Then we have to use MathBigIntegers to hold the answer
                return this.addSafe(that, narrow);
            }
            //else
            long ansL;
            try{
                ansL = Math.multiplyExact(this.n, thatL);
            }
            catch (ArithmeticException ignored) {
                return this.multiplySafe(that, narrow);
            }
            //else
            // Then the sum fits within signed long
            return narrowNexus(ansL, narrow);
        }
        //else
        return this.multiplySafe(that, narrow);
    }
    
    @NonNull
    @Contract(pure = true)
    private WholeNum multiplySafe(
            @NonNull WholeNum that,
            @NonNull NarrowSettings narrow
    ) {
        BigInteger thatBI = that.toBigInteger();
        MathBigInteger thisMBI = MathBigInteger.valueOf(that);
        return thisMBI.multiply(thatBI, narrow);
    }
    
    /**
     * Add this and another {@link SignedPrimitive},
     * putting the sum into another {@link LongSigned}.
     * @param that the other {@link SignedPrimitive} to add.
     * @return the {@link LongSigned} with the sum.
     * @throws ArithmeticException if the value over or underflows.
     */
    @NonNull
    @Contract(pure = true)
    public LongSigned multiplyStrict(
            @NonNull SignedPrimitive that
    ) throws ArithmeticException {
        // Get that long value
        long thatL = that.longValue();
        // Get sum
        long ansLong = Math.multiplyExact(this.n, thatL);
        // Return as LongSigned
        return valueOf(ansLong);
    }
    @NonNull
    protected LongSigned@NonNull[] divideAndRemainder(
            long that
    ) throws ArithmeticException {
        // Establish array to hold answer
        // Initial values for n = 0
        LongSigned[] ans = {
                CACHE.getMinMaxZero( (byte) 0 ),
                CACHE.getMinMaxZero( (byte) 0 )
        };
        // Check for cases that result in an input being returned
        // If dividing by 1
        if (that == 1) {
            ans[0] = this;
        } else if (that == this.n) {
            ans[0] = valueOf(1);
        } else if (-that == this.n) {
            ans[0] = valueOf(-1);
        } else if (that > this.n) {
            ans[1] = this;
        } else if (this.n != 0) {
            ans[0] = valueOf(this.n / that);
            ans[1] = valueOf(this.n % that);
        }
        // Return as MBIs
        return ans;
    }

    /**
     * Divide this by another {@link LongSigned},
     * putting the quotient and remainder
     * into an array {@link LongSigned}s.
     * Since division cannot grow, there is no need for an Exact version.
     * @param that the {@link LongSigned} to divide by.
     * @return an array of 2 {@link LongSigned}s;
     *              the quotient is first, followed by the remainder.
     */
    @NonNull
    @Contract(pure = true)
    public LongSigned@NonNull[] divideAndRemainder(
            @NonNull SignedPrimitive that
    ) {
        return this.quotientAndRemainder( that.longValue() );
    }

    /**
     * Divide this by a {@link LongSigned},
     * putting the quotient and remainder
     * into an array of {@link WholeNum}s.
     * The actual returned value types will be dependent on the narrowing settings.
     * @param that the {@link LongSigned} to divide by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}{@code .SAME_SIGN_TYPE}.
     * @param differentNarrows a {@code boolean} determining whether the narrowing will use
     *               the smallest type that the two can both fit it,
     *               or the smallest type for each one.
     * @return an array of 2 {@link WholeNum} numbers;
     *               the quotient is first, followed by the remainder.
     * @throws ArithmeticException if the answer is negative but set
     *               to be narrowed to unsigned.
     */
    @NonNull
    @Contract(pure = true)
    public WholeNum@NonNull[] divideAndRemainder(
            @NonNull SignedPrimitive that,
            @NonNull NarrowSettings narrow,
            boolean differentNarrows
    ) throws ArithmeticException {
        return (narrow == NarrowSettings.NONE) ?
                this.divideAndRemainder(that) :
                this.divideAndRemainder(
                        (WholeNum) that,
                        narrow,
                        differentNarrows
                );
    }

    /**
     * Divide this by a {@link WholeNum},
     * putting the quotient and remainder
     * into an array {@link LongSigned}s.
     * @param that the {@link WholeNum} to divide by.
     * @return an array of 2 {@link LongSigned}s;
     *              the quotient is first, followed by the remainder.
     */
    @NonNull
    @Contract(pure = true)
    public LongSigned@NonNull[] divideAndRemainder(
            @NonNull WholeNum that
    ) {
        return (LongSigned[]) ( this.divideAndRemainder(that, NarrowSettings.NONE, false) );
    }

    /**
     * Divide this by a {@link WholeNum},
     * putting the quotient and remainder
     * into an array of {@link WholeNum}s.
     * The actual returned value types will be dependent on the narrowing settings.
     * @param that the {@link WholeNum} to divide by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}{@code .SAME_SIGN_TYPE}.
     * @param differentNarrows a {@code boolean} determining whether the narrowing will use
     *               the smallest type that the two can both fit it,
     *               or the smallest type for each one.
     * @return an array of 2 {@link WholeNum} numbers;
     *               the quotient is first, followed by the remainder.
     * @throws ArithmeticException if the answer is negative but set
     *               to be narrowed to unsigned.
     */
    @NonNull
    @Contract(pure = true)
    public WholeNum@NonNull[] divideAndRemainder(
            @NonNull WholeNum that,
            @NonNull NarrowSettings narrow,
            boolean differentNarrows
    ) throws ArithmeticException {
    
    }

    /**
     * Subtract this by a {@code long},
     * putting the quotient into a {@link WholeNum}.
     * This only returns the quotient!
     * The {@link WholeNum} will be the narrowest such value that contains the answer.
     * @param that the {@code long} to divide by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} with the difference.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen.
     */
    protected WholeNum divide(
            long that,
            NarrowSettings narrow
    )
            throws ArithmeticException {
        // Get the quotient
        long ansBI = (this.n).divide(that);
        // Route the difference through the narrowNexus
        return narrowNexus(ansBI, narrow);
    }

    /**
     * Divide this by another {@link LongSigned},
     * putting the quotient into another {@link LongSigned}.
     * This only returns the quotient!
     * This version does not round, it truncates.
     * @param that the {@link LongSigned} to divide by.
     * @return a {@link WholeNum} with the quotient.
     */
    public LongSigned divide(LongSigned that) {
        // Get the quotient
        long ansBI = (this.n).divide(that.n);
        // Return a new LongSigned made of the quotient
        return new LongSigned(ansBI);
    }

    /**
     * Divide this by another {@link LongSigned},
     * putting the quotient into another {@link WholeNum}.
     * This only returns the quotient!
     * This version does not round, it truncates.
     * @param that the {@link LongSigned} to divide by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}.{@code SAME_SIGN_TYPE}.
     * @return a {@link WholeNum} with the quotient.
     * @throws ArithmeticException if the answer is negative but set
     *               to be narrowed to unsigned.
     */
    public WholeNum divide(
            LongSigned that,
            NarrowSettings narrow
    )
            throws ArithmeticException {
        // Pass to main divide function,
        // but turning that to a long
        return this.divide(that.n, narrow);
    }

    /**
     * Divide this by a {@link WholeNum},
     * putting the quotient into another {@link LongSigned}.
     * This only returns the quotient!
     * Since division cannot grow, there is no need for an Exact version.
     * @param that the {@link WholeNum} to divide by.
     * @return a {@link LongSigned} with the quotient.
     */
    public @NotNull LongSigned divide(@NotNull WholeNum that) {
        // Set NarrowSettings to NONE and use function with narrowing
        return (LongSigned)( this.divide(that, NarrowSettings.NONE) );
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
    public @NotNull WholeNum divide(
            @NotNull WholeNum that,
            NarrowSettings narrow
    )
            throws ArithmeticException {
        // Turn that into long
        long thatBI = that.tolong();
        // Pass to main divide function
        return this.divide(thatBI, narrow);
    }

    /**
     * Subtract this by a {@code long},
     * putting the remainder into a {@link WholeNum}.
     * This only returns the remainder!
     * The {@link WholeNum} will be the narrowest such value that contains the answer.
     * @param that the {@code long} to divide by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}{@code .SAME_SIGN_TYPE}.
     * @return the {@link WholeNum} with the difference.
     * @throws ArithmeticException if the value is negative but the unsigned narrowing type is chosen,
     *                             or if that is 0.
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    protected WholeNum remainder(
            long that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Get the remainder/mod
        long ansLong = this.n % that;
        // Route the remainder through the narrowNexus
        return narrowNexus(ansLong, narrow);
    }

    /**
     * Divide this by another {@link SignedPrimitive},
     * putting the remainder into another {@link LongSigned}.
     * This only returns the remainder!
     * @param that the {@link SignedPrimitive} to divide by.
     * @return a {@link LongSigned} with the remainder.
     * @throws ArithmeticException if that is 0.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public LongSigned remainder(
            @NotNull SignedPrimitive that
    ) {
        // Get that long
        long thatLong = that.longValue();
        // Get the remainder/mod
        // This is where the exception would be thrown
        long ansLong = this.n % thatLong;
        // Route the remainder through the narrowNexus
        return new LongSigned(ansLong);
    }

    /**
     * Divide this by another {@link SignedPrimitive},
     * putting the remainder into another {@link WholeNum}.
     * This only returns the remainder!
     * @param that the {@link SignedPrimitive} to divide by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}{@code .SAME_SIGN_TYPE}.
     * @return a {@link WholeNum} with the remainder.
     * @throws ArithmeticException if the answer is negative but set to be narrowed to unsigned,
     *                             or if that is 0
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public WholeNum remainder(
            @NotNull SignedPrimitive that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Get that long
        long thatLong = that.longValue();
        // Return the remainder/mod
        return this.remainder(thatLong, narrow);
    }

    /**
     * Divide this by a {@link WholeNum},
     * putting the remainder into another {@link LongSigned}.
     * This only returns the remainder!
     * Since division cannot grow, there is no need for an Exact version.
     * @param that the {@link WholeNum} to divide by.
     * @return a {@link LongSigned} with the remainder.
     * @throws ArithmeticException if that is 0.
     */
    @NotNull
    @Contract(pure = true)
    public LongSigned remainder(
            @NotNull WholeNum that
    )
            throws ArithmeticException {
        // Set NarrowSettings to NONE and use function with narrowing
        return (LongSigned)( this.remainder(that, NarrowSettings.NONE) );
    }

    /**
     * Divide this by a {@link WholeNum},
     * putting the remainder into another {@link WholeNum}.
     * This only returns the remainder!
     * This version does not round, it truncates.
     * @param that the {@link WholeNum} to divide by.
     * @param narrow the {@link NarrowSettings} type to use.
     *               Defaults to {@link NarrowSettings}{@code .SAME_SIGN_TYPE}.
     * @return a {@link WholeNum} with the remainder.
     * @throws ArithmeticException if the answer is negative but set to be narrowed to unsigned,
     *                             or if that is 0.
     */
    @NotNull
    @Contract(pure = true)
    public WholeNum remainder(
            @NotNull WholeNum that,
            @NotNull NarrowSettings narrow
    )
            throws ArithmeticException {
        // Dummy value to be overwritten
        long thatLong = 0;
        // For if modulus is greater than this
        boolean returnSame = false;
        // See if can fit as long
        try {
            thatLong = that.longValueExact();
            // This can only happen on an unsigned long that is over signed long max
            if (that instanceof NaturalNum &&
                    thatLong < 0)
                // Then that is inherently greater in magnitude than this anyway
                returnSame = true;
        }
        catch (ArithmeticException ignored) {
            returnSame = true;
        }
        if (returnSame)
            return (narrow == NarrowSettings.NONE) ?
                    this :
                    narrowNexus(n, narrow);
        //else
        return this.remainder(thatLong, narrow);
    }

    /**
     * Square this {@link IntegralNum}.
     * @return a {@link IntegralNum} with the square.
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public IntegralNum squared() {
        //Square it
        return this.multiply(this);
    }

    /**
     * Square this {@link LongSigned}.
     * @return a {@link LongSigned} with the square.
     * @throws ArithmeticException if the square does not fit.
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public LongSigned squaredStrict()
            throws ArithmeticException {
        //Square it
        return this.multiplyStrict(this);
    }

    /**
     * Give this {@link LongSigned}
     * to the power of the given {@code int}eger exponent.
     * @param exponent the {@code int} of the exponent.
     * @return a {@link LongSigned} with the result.
     */
    @NotNull
    @Contract(pure = true)
    public IntegralNum raised(
            @Range(from = 0, to = Integer.MAX_VALUE) int exponent
    )
            throws ArithmeticException {
        if (exponent < 0)
            throw new ArithmeticException("Negative power for integer value");
        //else
        switch (exponent) {
            case 0 -> {
                return new LongSigned(1);
            }
            case 1 -> {
                return this;
            }
            case 2 -> {
                return this.squared();
            }
            default -> {
                try {
                    long ansLong = n;
                    for (int i = 1; i < exponent; i++)
                        ansLong = Math.multiplyExact(ansLong, n);
                    return new LongSigned(ansLong);
                }
                catch (ArithmeticException ignored) {
                    BigInteger thisBI = BigInteger.valueOf(n);
                    BigInteger ansBI = thisBI.pow(exponent);
                    return new MathBigInteger(ansBI, null);
                }
            }
        }
    }

    /**
     * Give this {@link LongSigned}
     * to the power of the given positive {@code int}eger exponent.
     * @param exponent the {@code int} of the exponent.
     * @return a {@link LongSigned} with the result.
     * @throws ArithmeticException if exponent is negative.
     */
    @NotNull
    @Contract(pure = true)
    public LongSigned raisedStrict(
            @Range(from = 0, to = Integer.MAX_VALUE) int exponent
    )
            throws ArithmeticException {
        if (exponent < 0)
            throw new ArithmeticException("Negative power for integer value");
        //else
        switch (exponent) {
            case 0 -> {
                return new LongSigned(1);
            }
            case 1 -> {
                return this;
            }
            case 2 -> {
                return this.squaredStrict();
            }
            default -> {
                long ansLong = n;
                for (int i = 1; i < exponent; i++)
                    ansLong = Math.multiplyExact(ansLong, n);
                return new LongSigned(ansLong);
            }
        }
    }

    /**
     * Return this value as a {@code byte}.
     * Information will almost definitely be lost, however.
     * @return this value as a super-narrowly converted {@code byte} primitive.
     */
    @Contract(pure = true)
    public byte byteValue() {
        return (byte)n;
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
        // See if it is too big for signed byte
        if (n < Byte.MIN_VALUE ||
                n > Byte.MAX_VALUE)
            // Throw up if so
            throw new ArithmeticException("Value does not fit in byte");
        //else
        return (byte)n;
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
        // See if it is negative or too big for unsigned byte
        if (n < 0 ||
                n > ByteUnsigned.MAX_VALUE)
            // Throw up if so
            throw new ArithmeticException("Value does not fit in unsigned byte");
        //else
        return (byte)n;
    }

    /**
     * Return this value as a {@code short}
     * Information will quite very likely be lost, however.
     * @return this value as a very-narrowly converted {@code short} primitive
     */
    @Contract(pure = true)
    public short shortValue() {
        return (short)n;
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
        // See if it is too big for signed short
        if (n < Short.MIN_VALUE ||
                n > Short.MAX_VALUE)
            // Throw up if so
            throw new ArithmeticException("Value does not fit in short");
        //else
        return (short)n;
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
        // See if it is negative or too big for unsigned short
        if (n < 0 ||
                n > ShortUnsigned.MAX_VALUE)
            // Throw up if so
            throw new ArithmeticException("Value does not fit in unsigned short");
        //else
        return (short)n;
    }

    /**
     * Return this value as a {@code int}
     * Information will may very well be lost, however.
     * @return this value as a narrowly converted {@code int} primitive
     */
    @Contract(pure = true)
    public int intValue() {
        return (int)n;
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
        // See if it is too big for signed int
        if (n < Integer.MIN_VALUE ||
                n > Integer.MAX_VALUE)
            // Throw up if so
            throw new ArithmeticException("Value does not fit in int");
        //else
        return (int)n;
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
        // See if it is negative or too big for unsigned int
        if (n < 0 ||
                n > IntegerUnsigned.MAX_VALUE)
            // Throw up if so
            throw new ArithmeticException("Value does not fit in unsigned int");
        //else
        return (int)n;
    }

    /**
     * Return this value as a {@code long}
     * Information will might be lost, however.
     * @return this value as a narrowly converted {@code long} primitive
     */
    @Contract(pure = true)
    public long longValue() {
        return n;
    }

    /**
     * Return this value as a {@code long}, if it fits within
     * Or, raise an Arithmetic Exception if it doesn't
     * @return this value as a narrowly converted {@code long} primitive
     */
    @Contract(pure = true)
    public long longValueExact() {
        return n;
    }

    /**
     * Return this value as a {@code long}, if it can actually fit within
     * an <em>unsigned</em> {@code long}.
     * Or, much more likely, raise an Arithmetic Exception
     * if it doesn't fit
     * @return this value as a narrowly converted {@code long} primitive.
     *              This value is meant to be longerpreted as <b><em>unsigned</em></b>
     * @throws ArithmeticException if the value is negative.
     */
    @Contract(pure = true)
    public long longValueExactUnsigned()
            throws ArithmeticException {
        // See if it is negative
        if (n < 0)
            // Throw up if so
            throw new ArithmeticException("Negative value cannot be made unsigned");
        //else
        return n;
    }

    /**
     * Turns the contained value into a {@code float}.
     * Precision may be lost!
     * @return a {@code float} containing this value
     */
    @Contract(pure = true)
    public float floatValue() {
        return (float)n;
    }

    /**
     * Turns the contained value into a {@code double}.
     * Precision may be lost!
     * @return a {@code double} containing this value
     */
    @Contract(pure = true)
    public double doubleValue() {
        return (double)n;
    }

    /**
     * @return this value as a {@link BigInteger}.
     */
    @NotNull
    @Contract(pure = true)
    public BigInteger toBigInteger() {
        return BigInteger.valueOf(n);
    }

    /**
     * Overrides method toString() in class {@link Object}
     * @return the value within this object as a {@link String}
     */
    @Override
    @NotNull
    @Contract(pure = true)
    public String toString() {
        return Long.toString(n);
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
        return Long.toString(n, radix);
    }
}
