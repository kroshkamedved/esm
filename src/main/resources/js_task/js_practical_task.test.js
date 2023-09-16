const jsPracticeTask = require('./js_practical_task');

test('must return a date that comes in a predetermined number of seconds after 01.06.2020 00:00:002020',
    () => {
        expect(jsPracticeTask.secondsToDate(31536000)).toBe('01.06.2021');
        expect(jsPracticeTask.secondsToDate(0)).toBe('01.06.2020');
        expect(jsPracticeTask.secondsToDate(86400)).toBe('02.06.2020');
    }
);

test('function that returns a base 2 (binary) representation of a base 10 (decimal) string number',
    () => {
        expect(jsPracticeTask.toBase2Converter(5)).toBe('101');
        expect(jsPracticeTask.toBase2Converter(10)).toBe('1010');
        expect(jsPracticeTask.toBase2Converter(1024)).toBe('10000000000');
        expect(jsPracticeTask.toBase2Converter(1023)).toBe('1111111111');
        expect(jsPracticeTask.toBase2Converter(1022)).toBe('1111111110');
    }
)


test('You must create a function that takes two strings as arguments and returns the number of times the first string', 
    () => {
        expect(jsPracticeTask.substringOccurrencesCounter('a','test it')).toBe(0);
        expect(jsPracticeTask.substringOccurrencesCounter('t','test it')).toBe(3);
        expect(jsPracticeTask.substringOccurrencesCounter('T','test it')).toBe(3);
    }
)

test(' You must create a function that takes a string and returns a string in which each character is repeated once',
    ()=>{
        expect(jsPracticeTask.repeatingLitters('Hello')).toBe('HHeelloo');
        expect(jsPracticeTask.repeatingLitters('Hello world')).toBe('HHeelloo  wwoorrlldd');
    }
)

test('You must write a function redundant that takes in a string str and returns a function that returns str  Your function should return a function, not a string',
            () => {
                const f1 = jsPracticeTask.redundant('apple');
                expect(f1()).toBe('apple');

                const f2 = jsPracticeTask.redundant("pear");
                expect(f2()).toBe('pear');

                const f3 = jsPracticeTask.redundant("");
                expect(f3()).toBe('');
    }
)

test('You must create a function that multiplies two matricies (n x n each)', 
            () => {

                const arr1 = [[1,2,3],[4,5,6],[7,8,9]];
                const arr2 = [[1,2,3],[4,5,6]];
                const result = [[18,24,30],[54,69,84]]

                expect(jsPracticeTask.matrixMultiplication(arr1,arr2)).toEqual(result);
}

)


test('Create a gather function that accepts a string argument and returns another function gather("a")("b")("c").order(0)(1)(2).get() ➞ "abc"', 
            () => {
                expect(jsPracticeTask.gather("a")("b")("c").order(0)(1)(2).get()).toBe('abc');
                expect(jsPracticeTask.gather("a")("b")("c").order(2)(1)(0).get()).toBe('cba');
                expect(jsPracticeTask.gather("e")("l")("o")("l")("!")("h").order(5)(0)(1)(3)(2)(4).get()).toBe('hello!');
            }
)

