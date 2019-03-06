# useragent-parser-java
a high-efficiency useragent parser in server side

## Declaration

* based on [**uap-java**](https://github.com/ua-parser/uap-java)

* why have this projects?

  > * try make ua parsing more efficiency in server side
  > * more suitable in China

## Promotion

* `regexes` loaded only once on class loading
* using many `static` and `final` vars ot methods to make parsing thread safe *as best as one can*
* using LRUMap to **cache** frequently-used useragent, so it can be high-efficiency
* bring the intestine browsers regexes forward
* add **Wechat** matching
* you can change the default cache size in  the `Constants.CACHE_SIZE`



if have any questions leave me with a issue