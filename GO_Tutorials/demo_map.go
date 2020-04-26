package main

import (
	"fmt"
	"sort"
	"strconv"
	"strings"
)

func main(){
	// 均非空
	var map_init = map[int]string{1:"zhang",2:"wang"}
	//var map_make = make(map[int]string)
	map_init[3] = "gg"
	map_init[4] = "to_delete"
	delete(map_init,4)
	// 无序遍历
	keys:=make([]int,0,len(map_init))
	values:=make([]string,0,len(map_init))
	for k,v := range map_init{
		keys = append(keys,k)
		values = append(values,v)
	}
	sort.Ints(keys)
	sort.Strings(values)
	fmt.Printf("%d=>%s\n",keys,values)

	str := "hello,go,go,go"
	str_split := strings.Split(str,",")
	fmt.Println(str_split)
	fmt.Println(strings.Contains(str,"go"))
	// 是否包含任意一个字符
	fmt.Println(strings.ContainsAny(str,"asdo"))
	ss := []string{"a","ss","dd"}
	ss_new := strings.Join(ss,"-")
	fmt.Println(ss_new)

	sss := "100"
	ff, _ := strconv.ParseInt(sss, 10, 64)
	fmt.Printf("%d\n",ff)
}