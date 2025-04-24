
# JCalc

A fully functional calculator written in java that can parse any mathematical expression (with correct syntax)



## Functions Supported:
* All trigonometric functions
* logarithms: log(base, input)
* signum or sig,
* ceil and floor,
* power or pow,
* round
* absolute or abs

## Operations Supported
* Addition: +
* Subtraction: -
* Division: /
* Multiplication: *
* Power: ^
* Remainder: %
* Factorial: !

## Syntax

The calculator can parse all valid mathematical expressions and supports:
* Implicit multilication  `2log(2,8) is same as 2*log(2,8)`
* Parenthesis supported are `( [ {} ] )` (The ordering does not matter)

### [Console example](https://github.com/divyansh0x0/JCalc/blob/main/src/main/java/org/divyansh/examples/ConsoleCalculator.java):

```
     | |/ ___|  / \  | |   / ___|
  _  | | |     / _ \ | |  | |    
 | |_| | |___ / ___ \| |__| |___ 
  \___/ \____/_/   \_\_____\____|

Welcome to JCalc!
Write an expression and press enter to compute it OR write exit to quit
> sin(PI/6) + 2^(3*sqrt(144)) + 10*(2+3) + 4!
Solving sin(PI/6) + 2^(3*sqrt(144)) + 10*(2+3) + 4!
Result: 68719476810.5
> 
```

