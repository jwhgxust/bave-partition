package cn.edu.gxust.jiweihuang.kotlin.math.function.univariate

import org.hipparchus.analysis.differentiation.UnivariateDifferentiableFunction
import org.hipparchus.analysis.UnivariateFunction
import org.hipparchus.analysis.differentiation.DSFactory
import org.hipparchus.analysis.integration.*


/**
 * The interface [IUnivariateFunction] is used for representing univariate function.
 *
 * It inherits from [org.hipparchus.analysis.UnivariateFunction] interface of
 * hipparchus library which is powerful java math library.
 *
 * Create date:2019-02-17
 *
 * Last revision date:2019-03-08
 *
 * @author Jiwei Huang
 * @since 1.0.0
 * @see UnivariateFunction
 */
interface IUnivariateFunction : UnivariateFunction {
    /**
     * The string of univariate functional analytic expression.
     * for instance:`f(x)=a*x+b`
     */
    val formula: String
}

/**
 * The interface [IUnivariateDerivativeFunction] is used for
 * representing analytic derivative function of univariate function.
 *
 * Create date:2019-02-17
 *
 * Last revision date:2019-03-08
 *
 * @author Jiwei Huang
 * @since 1.0.0
 * @see IUnivariateFunction
 */
interface IUnivariateDerivativeFunction : IUnivariateFunction {
    /**
     * Get the calculated value of derivative function
     * at independent variable `x`.
     */
    fun derivative(x: Double): Double
}

/**
 * The interface [IUnivariateIntegralFunction] is used for
 * representing analytic integral function of univariate function.
 *
 * Create date:2019-02-17
 *
 * Last revision date:2019-03-08
 *
 * @author Jiwei Huang
 * @since 1.0.0
 * @see IUnivariateFunction
 */
interface IUnivariateIntegralFunction : IUnivariateFunction {
    /**
     * Get the calculated value of integral function
     * at independent variable `x`.
     *
     * The integral constant of integral function is set to `0.0`
     */
    fun integral(x: Double): Double

    /**
     * Get the definite integral calculated value of univariate function at
     * interval of independent variables `[lowerX,upperX]`.
     *
     * @throws IllegalArgumentException if `lowerX` or `upperX` is not finite.
     */
    fun integral(lowerX: Double, upperX: Double): Double {
        if (lowerX.isFinite() && upperX.isFinite()) {
            return integral(upperX) - integral(lowerX)
        } else {
            throw IllegalArgumentException("Expected the parameter {lowerX} and {upperX} is finite,but get {lowerX = $lowerX,upperX=$upperX}.")
        }
    }
}

/**
 * The interface [IUnivariateInverseFunction] is used for
 * representing inverse function of univariate function.
 *
 * Create date:2019-02-17
 *
 * Last revision date:2019-03-06
 *
 * @author Jiwei Huang
 * @since 1.0.0
 * @see IUnivariateFunction
 */
interface IUnivariateInverseFunction : IUnivariateFunction {
    /**
     * Get the calculated value of inverse function
     * at dependent variable `y`.
     */
    fun inverse(y: Double): DoubleArray
}

/**
 * The interface [IUnivariateDifferentiableFunction] is used for
 * representing differential of univariate function with
 * Rall's Number described in Dan Kalman's paper
 * [Doubly Recursive Multivariate Automatic Differentiation](http://www1.american.edu/cas/mathstat/People/kalman/pdffiles/mmgautodiff.pdf)
 *
 * Create date:2019-02-17
 *
 * Last revision date:2019-03-08
 *
 * @author Jiwei Huang
 * @since 1.0.0
 * @see IUnivariateFunction
 * @see UnivariateDifferentiableFunction
 */
interface IUnivariateDifferentiableFunction : IUnivariateFunction,
        UnivariateDifferentiableFunction {
    /**
     * Get the calculated differential with specified order.
     */
    fun differential(x: Double, order: Int = 1): Double {
        return value(DSFactory(1, order).variable(0,
                x)).getPartialDerivative(order)
    }
}

/**
 * The interface [IUnivariateIntegrableFunction] is used for
 * representing univariate integrable function with numerical
 * integration method .
 *
 * Create date:2019-02-17
 *
 * Last revision date:2019-03-08
 *
 * @author Jiwei Huang
 * @since 1.0.0
 * @see IUnivariateFunction
 * @see RombergIntegrator
 * @see SimpsonIntegrator
 * @see MidPointIntegrator
 * @see TrapezoidIntegrator
 * @see IterativeLegendreGaussIntegrator
 */
interface IUnivariateIntegrableFunction : IUnivariateFunction {

    /**
     * default integrate with optional integral algorithm specified with [String].
     *
     * @throws IllegalArgumentException if method is not one of `{"romberg","simpson","midpoint"
     * "trapezoid","iterativelegendregauss"}` that ignore case.
     */
    fun integrate(lowerX: Double, upperX: Double,
                  method: String): Double {
        return when (method.toLowerCase()) {
            "romberg" -> integrateRomberg(lowerX, upperX)
            "simpson" -> integrateSimpson(lowerX, upperX)
            "midpoint" -> integrateMidPoint(lowerX, upperX)
            "trapezoid" -> integrateTrapezoid(lowerX, upperX)
            "iterativelegendregauss" -> integrateIterativeLegendreGauss(lowerX, upperX)
            else -> throw IllegalArgumentException("Integral methods are not supported for $method")
        }
    }

    /**
     * default integrate with optional integral algorithm specified with enum [IntegralMethods].
     */
    fun integrate(lowerX: Double, upperX: Double,
                  methods: IntegralMethods = Companion.IntegralMethods.Romberg): Double {
        return when (methods) {
            Companion.IntegralMethods.Romberg -> integrateRomberg(lowerX, upperX)
            Companion.IntegralMethods.Simpson -> integrateSimpson(lowerX, upperX)
            Companion.IntegralMethods.MidPoint -> integrateMidPoint(lowerX, upperX)
            Companion.IntegralMethods.Trapezoid -> integrateTrapezoid(lowerX, upperX)
            Companion.IntegralMethods.IterativeLegendreGauss -> integrateIterativeLegendreGauss(lowerX, upperX)
        }
    }

    companion object {
        const val defaultMaxIter: Int = Int.MAX_VALUE
        const val defaultPointsNumberForIterativeLegendreGaussPointsNumber: Int = 32

        /**
         * The enum for integral methods.
         */
        enum class IntegralMethods {
            Romberg, Simpson, MidPoint, Trapezoid, IterativeLegendreGauss
        }
    }

    /** Romberg integral algorithm */
    fun integrateRomberg(lowerX: Double, upperX: Double, maxIter: Int = defaultMaxIter,
                         relativeAccuracy: Double = BaseAbstractUnivariateIntegrator.DEFAULT_RELATIVE_ACCURACY,
                         absoluteAccuracy: Double = BaseAbstractUnivariateIntegrator.DEFAULT_ABSOLUTE_ACCURACY,
                         minimalIterationCount: Int = BaseAbstractUnivariateIntegrator.DEFAULT_MIN_ITERATIONS_COUNT,
                         maximalIterationCount: Int = RombergIntegrator.ROMBERG_MAX_ITERATIONS_COUNT): Double {
        return RombergIntegrator(relativeAccuracy, absoluteAccuracy,
                minimalIterationCount, maximalIterationCount).integrate(maxIter, this, lowerX, upperX)
    }


    /** Simpson integral algorithm */
    fun integrateSimpson(lowerX: Double, upperX: Double, maxIter: Int = defaultMaxIter,
                         relativeAccuracy: Double = BaseAbstractUnivariateIntegrator.DEFAULT_RELATIVE_ACCURACY,
                         absoluteAccuracy: Double = BaseAbstractUnivariateIntegrator.DEFAULT_ABSOLUTE_ACCURACY,
                         minimalIterationCount: Int = BaseAbstractUnivariateIntegrator.DEFAULT_MIN_ITERATIONS_COUNT,
                         maximalIterationCount: Int = SimpsonIntegrator.SIMPSON_MAX_ITERATIONS_COUNT): Double {
        return SimpsonIntegrator(relativeAccuracy, absoluteAccuracy,
                minimalIterationCount, maximalIterationCount).integrate(maxIter, this, lowerX, upperX)
    }

    /** MidPoint integral algorithm */
    fun integrateMidPoint(lowerX: Double, upperX: Double, maxIter: Int = defaultMaxIter,
                          relativeAccuracy: Double = BaseAbstractUnivariateIntegrator.DEFAULT_RELATIVE_ACCURACY,
                          absoluteAccuracy: Double = BaseAbstractUnivariateIntegrator.DEFAULT_ABSOLUTE_ACCURACY,
                          minimalIterationCount: Int = BaseAbstractUnivariateIntegrator.DEFAULT_MIN_ITERATIONS_COUNT,
                          maximalIterationCount: Int = MidPointIntegrator.MIDPOINT_MAX_ITERATIONS_COUNT): Double {
        return MidPointIntegrator(relativeAccuracy, absoluteAccuracy,
                minimalIterationCount, maximalIterationCount).integrate(maxIter, this, lowerX, upperX)
    }


    /** Trapezoid integral algorithm */
    fun integrateTrapezoid(lowerX: Double, upperX: Double, maxIter: Int = defaultMaxIter,
                           relativeAccuracy: Double = BaseAbstractUnivariateIntegrator.DEFAULT_RELATIVE_ACCURACY,
                           absoluteAccuracy: Double = BaseAbstractUnivariateIntegrator.DEFAULT_ABSOLUTE_ACCURACY,
                           minimalIterationCount: Int = BaseAbstractUnivariateIntegrator.DEFAULT_MIN_ITERATIONS_COUNT,
                           maximalIterationCount: Int = TrapezoidIntegrator.TRAPEZOID_MAX_ITERATIONS_COUNT): Double {
        return TrapezoidIntegrator(relativeAccuracy, absoluteAccuracy,
                minimalIterationCount, maximalIterationCount).integrate(maxIter, this, lowerX, upperX)
    }


    /** Legendre-Gauss quadrature rule on sub-intervals of the integration range */
    fun integrateIterativeLegendreGauss(lowerX: Double, upperX: Double, maxIter: Int = defaultMaxIter,
                                        integrationPointsNumber: Int = defaultPointsNumberForIterativeLegendreGaussPointsNumber,
                                        relativeAccuracy: Double = BaseAbstractUnivariateIntegrator.DEFAULT_RELATIVE_ACCURACY,
                                        absoluteAccuracy: Double = BaseAbstractUnivariateIntegrator.DEFAULT_ABSOLUTE_ACCURACY,
                                        minimalIterationCount: Int = BaseAbstractUnivariateIntegrator.DEFAULT_MIN_ITERATIONS_COUNT,
                                        maximalIterationCount: Int = BaseAbstractUnivariateIntegrator.DEFAULT_MAX_ITERATIONS_COUNT): Double {
        return IterativeLegendreGaussIntegrator(integrationPointsNumber,
                relativeAccuracy, absoluteAccuracy, minimalIterationCount,
                maximalIterationCount).integrate(maxIter, this, lowerX, upperX)
    }

}