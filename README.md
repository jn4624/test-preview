# for-preview-groovy-console

```groovy
import java.util.concurrent.ConcurrentHashMap
import java.util.function.BiFunction
import java.util.function.Function
import java.util.stream.IntStream

// print
System.out.println("hello")

println("hello")
println "hello"
println 'hello'

// array
int[] jArray = new int[]{1, 2, 3, 4, 5}
def gArray = [1, 2, 3, 4, 5] as int[]
println jArray
println gArray
println jArray.equals(gArray)
println jArray == (gArray)
println jArray.is(gArray)
println jArray.is(jArray)

// list
List<Integer> jList = List.of(1, 2, 3, 4, 5)
var jList2 = List.of(1, 2, 3, 4, 5)
List<Integer> gList = [1, 2, 3, 4, 5]
def gList2 = [1, 2, 3, 4, 5]
println jList
println jList2
println gList
println gList2
println gList.getClass()
println gList2.getClass()
println jList.get(0)
println jList.get(4)
println gList[0]
println gList[4]
println gList[-1]
println gList[-5]

// map
Map<String, Object> jMap = Map.of("name", "pro", "age", 20)
println jMap.get("name")
println jMap.get("age")

def gMap = [name: "pro", age: 20]
println gMap.get("name")
println gMap.get("age")
println gMap.getClass()

def cMap = new ConcurrentHashMap([name: "pro", age: 20])
println cMap.get("name")
println cMap.get("age")
println cMap.getClass()

def cMap2 = [name: "pro", age: 20] as ConcurrentHashMap
println cMap2.getClass()

// string format
String word = "world"
println String.format("hello + %s", word)

println "hello + $word"
println "hello + ${word + '!'}"
println 'hello + $word'

// range
IntStream.rangeClosed(1, 10).forEach (v -> System.out.print(v));

(1..10).each {v -> print(v)}
(1..10).each {print it}

// if
def value = 20
if (value in 10..30) {
    println "$value is between 10 ~ 30"
}

// for
for (int i = 0; i < 5; i++) {
    println "idx : " + i
}

for (i in 0..<5) {
    println "idx : " + i
}

// foreach
var jListByForeach = Arrays.asList(1, 2, 3)
for (Integer a : jListByForeach) {
    println a
}

for (def a : jListByForeach) {
    println a
}

for (def a : [1, 2, 3]) {
    println a
}

// switch
int switchValue = 12;
var jResultBySwitch = switch (switchValue) {
    case 10, 11, 12, 13, 14 -> {
        println switchValue
        yield switchValue * 2
    }
    default -> 0
}
println jResultBySwitch

def gResultBySwitch = switch (switchValue) {
    case 10..14 -> {
        println switchValue
        yield switchValue * 2
    }
    default -> 0
}
println gResultBySwitch

def res = 'ok'
switch (res) {
    case ~/ok|yes|sure/ -> {
        println 'positive'
    }
    default -> 'none'
}

// null, empty, '' 은 false 처리
"".isEmpty() ? println('empty') : println('non empty')
"" ? println('non empty') : println('empty')

var list = Collections.emptyList();
list.isEmpty() ? println('empty') : println('non empty')
list ? println('non empty') : println('empty')

var obj = null;
obj == null ? println('null') : println('non null')
obj ? println('non null') : println('null')

// null safe
class Person {
    String name;
}

Person person = null;
println person != null ? person.name : 'invalid'

println person?.name ?: 'invalid'
println person?.name

// 그루비에서는 모든 것이 객체
println 5 + 2
println 5.plus(2)

// 람다와 클로저
BiFunction<Long, Long, Long> jSum = (Long a, Long b) -> a + b;
println jSum.apply(5, 5);

def gSum = {a, b -> a + b}
println gSum(5, 5)

// 함수 합성
Function<Long, Long> addFive1 = a -> a + 5
Function<Long, Long> mulByTwo1 = a -> a * 2
Function<Long, Long> combined1 = addFive1.andThen(mulByTwo1)
println combined1.apply(2)

def addFive2 = {a -> a + 5}
def mulByTwo2 = {a -> a * 2}
def combined2 = addFive2 >> mulByTwo2
println combined2(2)

// 커링 -> 자바는 커링 미지원 -> 강제로 커링처럼 보이게끔 구현 필요
Function<Long, Function<Long, Long>> jSumForCurry = a -> (Long b) -> a + b;
Function<Long, Long> jAddFive = jSumForCurry(5)
println jAddFive.apply(5)

def gSumAB = {a, b -> a + b}
def gSumB = gSumAB.curry(5)
println gSumB(5)

// 문자열 함수
def "hello world"() {
    println "hello"
}
"hello world"()
```
