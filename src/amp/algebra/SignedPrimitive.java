package amp.algebra;

import java.math.BigInteger;

import org.jetbrains.annota

public abstract class SignedPrimitive
        extends IntegralNum {
    /**
     * Static {@link BigInteger} with largest possible SignedPrimitive
     */
    public static final BigInteger MAX_VALUE = BigInteger.valueOf(Long.MAX_VALUE);

    /**
     * Static {@link BigInteger} with largest possible negative SignedPrimitive
     */
    public static final BigInteger MIN_VALUE = BigInteger.valueOf(Long.MIN_VALUE);

    /**
     * {@link MathBigInteger} of
     * {@link Long}{@code .MAX_VALUE + 1 = -}{@link Long}{@code .MIN_VALUE}
     */
    protected static final MathBigInteger LONG_MIN_NEGATED =
            new MathBigInteger(
                    (SignedPrimitive.MIN_VALUE).negate(),
                    null
            );

    /**
     * Copies this {@link SignedPrimitive} but with the opposite sign
     * into another {@link SignedPrimitive}.
     * @return the negated {@link SignedPrimitive}
     * @throws ArithmeticException if the negation does not fit as a signed long.
     */
    @NotNull
    @Contract(value = " -> new", pure = true)
    public abstract SignedPrimitive negateStrict()
            throws ArithmeticException;

    /**
     * Copies this {@link SignedPrimitive} but with a positive sign
     * into another {@link SignedPrimitive}.
     * @return the negated {@link SignedPrimitive}
     * @throws ArithmeticException if the negation does not fit as a signed long.
     */
    @NotNull
    @Contract(pure = true)
    public abstract SignedPrimitive absStrict()
            throws ArithmeticException;

    /**
     * Add this and another {@link SignedPrimitive},
     * putting the sum into another {@link LongSigned}.
     * @param that the other {@link SignedPrimitive} to add.
     * @return the {@link LongSigned} with the sum.
     * @throws ArithmeticException if the value over or underflows.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public abstract LongSigned addStrict(
            @NotNull SignedPrimitive that
    )
            throws ArithmeticException;

    /**
     * Subtract this by another {@link SignedPrimitive},
     * putting the difference into another {@link LongSigned}.
     * @param that the other {@link SignedPrimitive} to subtract by.
     * @return the {@link LongSigned} with the difference.
     * @throws ArithmeticException if the value over or underflows.
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public abstract LongSigned subtractStrict(
            @NotNull SignedPrimitive that
    )
            throws ArithmeticException;
}
