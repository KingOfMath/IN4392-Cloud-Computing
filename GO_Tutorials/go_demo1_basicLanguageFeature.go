package main

import (
	"fmt"
	"math"
	"runtime"
)

func pow(x, n, limit float64) float64 {
	if v := math.Pow(x, n); v < limit {
		return v
	} else {
		fmt.Printf("%g >= %g\n", v, limit)
	}

	return limit
}

func main() {
	sum := 0
	for i := 0; i < 20; i++ {
		sum += i
	}

	sum1 := 1
	for ; sum1 < 20; {
		sum1 += sum1
	}
	fmt.Println(sum, sum1)

	fmt.Println(
		pow(3, 2, 10),
		pow(3, 3, 20),
	)

	switch os := runtime.GOOS; os {
	case "darwin":
		fmt.Println("OS X.")
	case "linux":
		fmt.Println("Linux.")
	default:
		// freebsd, openbsd,
		// plan9, windows...
		fmt.Printf("%s.", os)
	}

	fmt.Println("counting")
	for i := 0; i < 3; i++ {
		defer fmt.Println(i)
	}
	fmt.Println("done")

}
