package main

import "fmt"

type G struct {
	name string
}

type Ge int

func (a G) Print() { //(a G) 表示 Receiver 接收者 方法使用者
	fmt.Println("GG")
}

func (a *Ge) Print() {
	fmt.Println("GGGG")
}

func (ge *Ge) Add(num int) {
	*ge += Ge(num)
}

func main() {
	b := G{}
	b.Print()
	var c Ge // 为int型可以进行输出
	c.Print()
	(*Ge).Print(&c) // 直接用类型进行输出，巧妙
	var t Ge
	t.Add(100)
	fmt.Println(t)
}
