package main

import "fmt"

func main(){
	// 定长，非引用
	arr := [4]int{1,2,3,4}
	arr_copy := arr
	arr[0] = 100
	fmt.Println(arr_copy)

	// 变长
	s1 := []int{1,2,3}
	s1_copy := s1
	s1[0] = 20

	// make, 添加元素用...
	s2_var := make([]int,2,6)
	s2_var = append(s2_var,s1_copy...)
	fmt.Println(s2_var)

	// 遍历
	for i,v := range s2_var{
		fmt.Printf("%d=>%d\n",i,v)
	}
}
