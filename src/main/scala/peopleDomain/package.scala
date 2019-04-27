package object peopleDomain {
  case class Person(name: String)
  case class PersonWithCash(person: Person, cash: BigDecimal)
}
