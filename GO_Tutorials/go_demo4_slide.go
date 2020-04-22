package main

import "fmt"

func main(){

	num := []int{0,1,2,3,4,5,6}
	var numbers = make([]int,len(num),cap(num)*2)
	copy(numbers,num) // numbers <- num 反的!
	numbers = append(numbers, 3,4,5)

	fmt.Println(num)
	fmt.Println(numbers)
}