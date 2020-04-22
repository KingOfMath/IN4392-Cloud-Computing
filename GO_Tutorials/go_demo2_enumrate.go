package main

import "fmt"

const (
	B  float64 = 0
	KB float64 = 1 << (iota * 2)
	MB
	GB
	TB
)

func main() {
	fmt.Println(B)
	fmt.Println(KB)
	fmt.Println(MB)
	fmt.Println(GB)
	fmt.Println(TB)
}
