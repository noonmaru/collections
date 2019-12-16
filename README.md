# Noonmaru collections library
[![Build Status](https://travis-ci.org/noonmaru/collections.svg?branch=master)](https://travis-ci.org/noonmaru/collections)
[![Coverage Status](https://coveralls.io/repos/github/noonmaru/collections/badge.svg?branch=master)](https://coveralls.io/github/noonmaru/collections?branch=master)
[![Maintainability](https://api.codeclimate.com/v1/badges/62488473d09f07d0935a/maintainability)](https://codeclimate.com/github/noonmaru/collections/maintainability)
[![](https://jitpack.io/v/noonmaru/collections.svg)](https://jitpack.io/#noonmaru/collections)
![JitPack - Downloads](https://img.shields.io/jitpack/dm/github/noonmaru/collections)
![GitHub](https://img.shields.io/github/license/noonmaru/collections)
![Twitch Status](https://img.shields.io/twitch/status/hptgrm)

> * #####Features
> 	```
>   Node - 노드기반 컬렉션 인터페이스
>   LinkedNodeList - NodeCollection을 구현한 List
>   EventNodeList - Link, Unlink 이벤트를 확인 가능한 LinkedNodeList
>   RandomBox - 요소 빈도에 따라 무작위로 반환하는 컨테이너
>   ```
>  <br>
> * #####Gradle
>   ```groovy
>   allprojects {
>       repositories {
>      	...
>       	maven { url 'https://jitpack.io' }
>       }
>   }
>   ```
>   ```groovy
>   dependencies {
>       implementation 'com.github.noonmaru:collections:Tag'
>  	}
>   ```