'use strict';

/**
 * You must return a date that comes in a predetermined number of seconds after 01.06.2020 00:00:002020
 * @param {number} seconds
 * @returns {Date}
 *
 * @example
 *      31536000 -> 01.06.2021
 *      0 -> 01.06.2020
 *      86400 -> 02.06.2020
 */
function secondsToDate(seconds) {
    let entryPointDate = new Date('2020-06-01T00:00:00.2020Z');
    let result = new Date(entryPointDate.getTime() + seconds*1000);
    
    let year = result.getFullYear();
    let month = String(result.getMonth()+1).padStart(2,0);
    let day = String(result.getDate()).padStart(2,0);

    return `${day}.${month}.${year}`;
}


/**
 * You must create a function that returns a base 2 (binary) representation of a base 10 (decimal) string number
 * ! Numbers will always be below 1024 (not including 1024)
 * ! You are not able to use parseInt
 * @param {number} decimal
 * @return {string}
 *
 * @example
 *      5 -> "101"
 *      10 -> "1010"
 */
function toBase2Converter(decimal) {
    let result ='';
    return writeToString(result,decimal)
}
function writeToString(str, num){
    if(num === 0) return str;
    else{
        str = writeToString(str, num >> 1);
        str+=num%2;
        return str;
    }
}

/**
 * You must create a function that takes two strings as arguments and returns the number of times the first string
 * is found in the text.
 * @param {string} substring
 * @param {string} text
 * @return {number}
 *
 * @example
 *      'a', 'test it' -> 0
 *      't', 'test it' -> 2
 *      'T', 'test it' -> 2
 */
function substringOccurrencesCounter(substring, text) {
    let index = 0;
    let counter = 0;
    text= text.toLowerCase();
    substring= substring.toLowerCase();
    while(text.indexOf(substring,index) != -1){
        index = text.indexOf(substring,index) + substring.length;
        counter+=1;
    }
    return counter;
}

/**
 * You must create a function that takes a string and returns a string in which each character is repeated once.
 *
 * @param {string} string
 * @return {string}
 *
 * @example
 *      "Hello" -> "HHeelloo"
 *      "Hello world" -> "HHeello  wworrldd" // o, l is repeated more then once. Space was also repeated
 */
function repeatingLitters(string) {
    let cursor = 0;
    let resultString = '';

    for(;cursor<string.length;cursor++){
        let currentChar = string.charAt(cursor)
        if( currentChar == string.charAt(cursor+1)){
            cursor++;
        }
        resultString+=currentChar+currentChar;
    }
    return resultString;
}

/**
 * You must write a function redundant that takes in a string str and returns a function that returns str.
 * ! Your function should return a function, not a string.
 *
 * @param {string} str
 * @return {function}
 *
 * @example
 *      const f1 = redundant("apple")
 *      f1() ➞ "apple"
 *
 *      const f2 = redundant("pear")
 *      f2() ➞ "pear"
 *
 *      const f3 = redundant("")
 *      f3() ➞ ""
 */
function redundant(str) {
    return () => str;
}

/**
 * https://en.wikipedia.org/wiki/Tower_of_Hanoi
 *
 * @param {number} disks
 * @return {number}
 */
function towerHanoi(disks) {

}

/**
 * You must create a function that multiplies two matricies (n x n each).
 *
 * @param {array} matrix1
 * @param {array} matrix2
 * @return {array}
 *
 */
function matrixMultiplication(matrix1, matrix2) {
    if(matrix1.length != matrix2[0].length) throw new Error('unacceptable matrixes for multiplication')

    let resultWidth = matrix2.length;
    let resultHeight = matrix1[0].length;

    let resultMatrix = new Array(resultWidth);

    for(let i = 0; i < resultWidth; i++){
        resultMatrix[i] = new Array(matrix1[0].length);
        for(let x = 0; x < resultHeight; x++){
            resultMatrix[i][x] = 0;
        }
    }

    for(let x = 0; x < resultHeight; x ++ ){
        for(let y = 0; y < resultWidth; y ++){
            for(let j = 0; j < matrix1.length;j ++ ){
                resultMatrix[y][resultHeight-x-1] += matrix1[j][matrix1[0].length -x-1] * matrix2[y][resultHeight-j-1];
            }
        }
    }
    console.log(resultMatrix);
    return resultMatrix;
}

/**
 * Create a gather function that accepts a string argument and returns another function.
 * The function calls should support continued chaining until order is called.
 * order should accept a number as an argument and return another function.
 * The function calls should support continued chaining until get is called.
 * get should return all of the arguments provided to the gather functions as a string in the order specified in the order functions.
 *
 * @param {string} str
 * @return {string}
 *
 * @example
 *      gather("a")("b")("c").order(0)(1)(2).get() ➞ "abc"
 *      gather("a")("b")("c").order(2)(1)(0).get() ➞ "cba"
 *      gather("e")("l")("o")("l")("!")("h").order(5)(0)(1)(3)(2)(4).get()  ➞ "hello"
 */
function gather(str) {

    let result = [str];
    let ordering = [];

    function add(str2){
        result.push(str2);
        return add;
    }

    function order(index){
        ordering.push(index);
        return Object.assign(order,{get})
    }
    function get(){
        return ordering.map((index) => result[index]).join('');
    }
    return Object.assign(add,{order,get})
}

module.exports = {
    secondsToDate,
    toBase2Converter,
    substringOccurrencesCounter,
    repeatingLitters,
    redundant,
    matrixMultiplication,
    gather
}