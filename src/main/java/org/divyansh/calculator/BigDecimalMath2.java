package org.divyansh.calculator;

import ch.obermuhlner.math.big.BigDecimalMath;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalMath2 {
    //    public static final int DIVISION_SCALE = 16;
    private static final RoundingMode PREFERRED_ROUNDING_MODE = RoundingMode.HALF_EVEN;//3.141592653591
    private static final String PI_STR = "3.141592653589793238462643383279502884197169399375105820974944592307816406286" +
            "208998628034825342117067982148086513282306647093844609550582231725359408128481" +
            "117450284102701938521105559644622948954930381964428810975665933446128475648233" +
            "786783165271201909145648566923460348610454326648213393607260249141273724587006" +
            "606315588174881520920962829254091715364367892590360011330530548820466521384146" +
            "951941511609433057270365759591953092186117381932611793105118548074462379962749" +
            "567351885752724891227938183011949129833673362440656643086021394946395224737190" +
            "702179860943702770539217176293176752384674818467669405132000568127145263560827" +
            "785771342757789609173637178721468440901224953430146549585371050792279689258923" +
            "542019956112129021960864034418159813629774771309960518707211349999998372978049" +
            "951059731732816096318595024459455346908302642522308253344685035261931188171010" +
            "003137838752886587533208381420617177669147303598253490428755468731159562863882" +
            "353787593751957781857780532171226806613001927876611195909216420198938095257201" +
            "065485863278865936153381827968230301952035301852968995773622599413891249721775" +
            "283479131515574857242454150695950829533116861727855889075098381754637464939319" +
            "255060400927701671139009848824012858361603563707660104710181942955596198946767" +
            "837449448255379774726847104047534646208046684259069491293313677028989152104752" +
            "162056966024058038150193511253382430035587640247496473263914199272604269922796" +
            "782354781636009341721641219924586315030286182974555706749838505494588586926995" +
            "690927210797509302955321165344987202755960236480665499119881834797753566369807" +
            "426542527862551818417574672890977772793800081647060016145249192173217214772350" +
            "141441973568548161361157352552133475741849468438523323907394143334547762416862" +
            "518983569485562099219222184272550254256887671790494601653466804988627232791786";
    private static final int OUTPUT_PRECISION = 10;
    public static final MathContext MATH_CONTEXT = new MathContext(128,RoundingMode.HALF_EVEN);
    private static final BigDecimal MINUS_ONE = BigDecimal.ONE.negate();
    public static final BigDecimal PI = new BigDecimal(getPi(50));


    public static final BigDecimal TWO_PI = PI.multiply(BigDecimal.TWO);
    public static final BigDecimal HALF_PI = PI.divide(BigDecimal.TWO, MATH_CONTEXT);
    private static final BigDecimal ONE_AND_HALF_PI = HALF_PI.multiply(BigDecimal.valueOf(3));
    private static final BigDecimal[] factorials = new BigDecimal[]{BigDecimal.ONE, BigDecimal.ONE, BigDecimal.TWO, BigDecimal.valueOf(6), BigDecimal.valueOf(24), BigDecimal.valueOf(120),
            BigDecimal.valueOf(720), BigDecimal.valueOf(5040), BigDecimal.valueOf(40320), BigDecimal.valueOf(362880), BigDecimal.valueOf(3628800),
            BigDecimal.valueOf(39916800), BigDecimal.valueOf(479001600), BigDecimal.valueOf(6227020800L), BigDecimal.valueOf(87178291200L), BigDecimal.valueOf(1307674368000L),
            BigDecimal.valueOf(20922789888000L), BigDecimal.valueOf(355687428096000L), BigDecimal.valueOf(6402373705728000L), BigDecimal.valueOf(121645100408832000L), BigDecimal.valueOf(2432902008176640000L),
            BigDecimal.valueOf(51090942171709400000D)
    };


    public static @NotNull BigDecimal getFactorial(BigDecimal bigDecimal) {
        if (bigDecimal.remainder(BigDecimal.ONE,MATH_CONTEXT).compareTo(BigDecimal.ZERO) != 0)
            throw new RuntimeException("Cannot find factorial of " + bigDecimal + " as it is not a whole number");
        double fact = 1;

        double val = bigDecimal.doubleValue();

        if (Double.isFinite(val) && val < 171) {

            while (val > 0) {
                fact *= val;
                val--;
            }
        } else {
            int i = 1;
            while (i < 171) {
                fact *= val;
                i++;
            }
            BigDecimal bigDecimalFact = BigDecimal.valueOf(fact);
            while (i < val) {
                bigDecimalFact= bigDecimalFact.multiply(BigDecimal.valueOf(i));
                i++;
            }
        }
        return BigDecimal.valueOf(fact);
    }

    public static BigDecimal sin(BigDecimal x) {
        if (x.remainder(TWO_PI, MATH_CONTEXT).compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (x.remainder(PI, MATH_CONTEXT).compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        double val = x.doubleValue();
        if (Double.isFinite(val)) {
            return BigDecimal.valueOf(Math.sin(val)).setScale(OUTPUT_PRECISION,PREFERRED_ROUNDING_MODE);
        }
        //x-x^3/3! + x^5/5!-x^7/7! +... = sum: (-1)^2i+1 * -(x^(2i + 1))/(2i+1)!
        final int numOfTerms = 6;
        BigDecimal sin = x;
        for (int i = 1; i < numOfTerms; i++) {
            int k = 2 * i + 1;
            BigDecimal term = x.pow(k).divide(factorials[k], MATH_CONTEXT);
            if (i % 2 != 0) { //Every odd index gets negated
                term = term.negate();
            }
            sin = sin.add(term);
        }
        return sin.setScale(OUTPUT_PRECISION, PREFERRED_ROUNDING_MODE);

    }

    public static BigDecimal cos(BigDecimal x) {
        if (x.remainder(TWO_PI, MATH_CONTEXT).compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ONE;
        }
        if (x.remainder(PI, MATH_CONTEXT).compareTo(BigDecimal.ZERO) == 0) {
            return MINUS_ONE;
        }
        double val = x.doubleValue();
        if (Double.isFinite(val)) {
            return BigDecimal.valueOf(Math.cos(val)).setScale(OUTPUT_PRECISION,PREFERRED_ROUNDING_MODE);
        }
        //1-x^2/2! + x^4/4!-x^6/6! +...
        final int numOfTerms = 6;
        BigDecimal cos = BigDecimal.ONE;
        for (int i = 1; i < numOfTerms; i++) {
            int k = 2 * i;
            BigDecimal term = x.pow(k).divide(factorials[k], MATH_CONTEXT);
            if (i % 2 != 0) { //Every odd index gets negated
                term = term.negate();
            }
            cos = cos.add(term);
        }
        return cos.setScale(OUTPUT_PRECISION, PREFERRED_ROUNDING_MODE);

    }

    @Contract(pure = true)
    public static BigDecimal signum(@NotNull BigDecimal x) {
        return switch (x.compareTo(BigDecimal.ZERO)) {
            case 0 -> BigDecimal.ZERO;
            case 1 -> BigDecimal.ONE;
            default -> MINUS_ONE;
        };
    }

    /**
     * Log with base e
     *
     * @param x argument of log
     */
    public static BigDecimal ln(BigDecimal x) {
        //log(x) = 2[(x - 1)/(x+1) + (1/3)(x-1)(x+1)+ 1/5(x-1)(x+1) + ...]

        double val = x.doubleValue();
        if (Double.isFinite(val)) {
            return BigDecimal.valueOf(Math.log(val)).setScale(OUTPUT_PRECISION,PREFERRED_ROUNDING_MODE);
        }

        final int numOfTerms = 5;
        BigDecimal common = x.subtract(BigDecimal.ONE).divide(x.add(BigDecimal.ONE), MATH_CONTEXT);
        BigDecimal log = common;
        for (int i = 1; i < numOfTerms; i++) {
            BigDecimal coeff = BigDecimal.ONE.divide(BigDecimal.valueOf(2L * i + 1L), MATH_CONTEXT);
            log = log.add(common.multiply(coeff));
        }
        return log.multiply(BigDecimal.TWO).setScale(OUTPUT_PRECISION,PREFERRED_ROUNDING_MODE);
    }

    public static BigDecimal ceil(BigDecimal arg0) {
        return arg0.setScale(0,RoundingMode.CEILING);
    }

    public static BigDecimal floor(BigDecimal arg0) {
        return arg0.setScale(0,RoundingMode.FLOOR);
    }

    public static BigDecimal pow(BigDecimal a, BigDecimal exp) {
        //a^x = 1 +xlna + x^2/2! (lnA)^2 + x^3/3! (lna)^3
        double valA = a.doubleValue();
        double valEXP = exp.doubleValue();
        if (Double.isFinite(valEXP) && Double.isFinite(valA)) {
            double p = Math.pow(valA,valEXP);
            if(Double.isFinite(p))
                return BigDecimal.valueOf(p).setScale(OUTPUT_PRECISION,PREFERRED_ROUNDING_MODE);
        }

        final int numOfTerms = 11;
        BigDecimal loga = ln(a);
        BigDecimal log = BigDecimal.ONE;
        for (int i = 1; i < numOfTerms; i++) {
            BigDecimal term = exp.pow(i).multiply(loga.pow(i)).divide(factorials[i], MATH_CONTEXT);
            log = log.add(term);
        }
        return log.setScale(OUTPUT_PRECISION,PREFERRED_ROUNDING_MODE);
    }

    private static String getPi(int digits) {
        return PI_STR.substring(0, digits);
    }

    public static BigDecimal log(BigDecimal arg0, BigDecimal arg1) {
        return BigDecimalMath.log(arg1,MATH_CONTEXT).divide(BigDecimalMath.log(arg0,MATH_CONTEXT), MATH_CONTEXT).setScale(OUTPUT_PRECISION,PREFERRED_ROUNDING_MODE);
    }

    public static BigDecimal lnNewton(BigDecimal x) {
        int ITER= 20;
        if (x.equals(BigDecimal.ONE)) {
            return BigDecimal.ZERO;
        }

        x = x.subtract(BigDecimal.ONE);
        BigDecimal ret = new BigDecimal(ITER + 1);
        for (long i = ITER; i >= 0; i--) {
            BigDecimal N = new BigDecimal(i / 2 + 1).pow(2);
            N = N.multiply(x, MATH_CONTEXT);
            ret = N.divide(ret, MATH_CONTEXT);

            N = new BigDecimal(i + 1);
            ret = ret.add(N, MATH_CONTEXT);

        }

        ret = x.divide(ret, MATH_CONTEXT);
        return ret;
    }
}
