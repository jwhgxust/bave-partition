package cn.edu.gxust.jiweihuang.kotlin.math.stat.distribution

import cn.edu.gxust.jiweihuang.kotlin.math.function.univariate.IUnivariateIntegrableFunction

import org.hipparchus.special.Gamma
import java.lang.Math.pow
import java.lang.Math.sqrt
import kotlin.math.pow

class FinitedNormalDistribution(mean: Double = 1.0, val halfLimit: Double,val order: Int = 3,
                                solverAbsoluteAccuracy: Double = DEFAULT_SOLVER_ABSOLUTE_ACCURACY) :
        RestrictedNormalDistribution(mean, sqrt(pow(halfLimit, 2.0) / ((2.0 * order) + 3.0)), halfLimit, solverAbsoluteAccuracy),
        IUnivariateIntegrableFunction {
    override val formula: String
        get() = ""

    override fun value(x: Double): Double {
        return density(x)
    }

    override fun getNumericalMean(): Double {
        return mean
    }

    override fun getNumericalVariance(): Double {
        return pow(sd, 2.0)
    }

    fun densityCoef(order: Int): Double {
        return (pow(2.0, -0.5 * pow(Math.sin(order * Math.PI), 2.0)) * pow(Math.PI, -0.5 * pow(Math.cos(order * Math.PI), 2.0)) * Gamma.gamma(1.5 + order)) / (Gamma.gamma(1.0 + order))
    }

    override fun density(x: Double): Double {
        return densityCoef(order) * (((1.0 - pow((x - mean) / halfLimit, 2.0)).pow(order)) / halfLimit)
    }

    override fun cumulativeProbability(x: Double): Double {
        return integrate(lowerLimit,x)
    }
}

fun main() {
    val  dist = FinitedNormalDistribution(1.0,6.0,4)
    println(dist.cumulativeProbability(3.0))
}