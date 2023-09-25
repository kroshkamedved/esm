const couponGrid = document.querySelector('.coupon-grid');
const loader = document.getElementById('loader');
const couponCount = document.getElementById('coupon-count');
const couponTotal = document.getElementById('coupon-total');

const couponLimit = 90;
couponTotal.innerHTML = couponLimit;

const couponIncrease = 10;
const pageCount = Math.ceil(couponLimit/couponIncrease);

let currentPage = 1;

const fs = require('fs');
const path = require('path');
const fileDir = '/Users/kroshkamedved/IdeaProjects/esm/src/main/resources/static'

fs.readFile('/Users/kroshkamedved/IdeaProjects/esm/src/main/resources/static/output.json','utf-8',(err,content) => {
    if(err){
        console.error('Error reading the file:', err);
        return;
    }
    try{
        const jsonArray = JSON.parse(content);
        localStorage.setItem('AllCertificates',JSON.stringify(jsonArray));
        console.log('jsons saved to the localhost');
    } catch(error){
        console.log('Error parsing JSON',error)
    }
})

const deserializeCoupon = (index,  ) => {
    const coupon = document.createElement('div');
    coupon.className = 'coupon-unit';

    const imgDiv = document.createElement('div');
    imgDiv.className = 'coupon-image-placeholder';
}