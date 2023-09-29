

const couponGrid = document.querySelector('.coupon-grid');
const loader = document.getElementById('loader');
const couponCount = document.getElementById('coupon-count');
const couponTotal = document.getElementById('coupon-total');

let allCollection = JSON.parse(localStorage.getItem('couponCollection'));
allCollection.sort((a, b) => {
    const creationDateA = new Date(a.creationDate);
    const creationDateB = new Date(b.creationDate);
    return creationDateA - creationDateB;
})

const categories = [];
for (let i = 0; i < allCollection.length; i++) {
    for (let y = 0; y < allCollection[i].tags.length; y++) {
        if (categories.includes(allCollection[i].tags[y])) {
            continue;
        }
        categories.push(allCollection[i].tags[y]);
    }
}

const finalCollection = allCollection;

const couponLimit = allCollection.length;
couponTotal.innerHTML = couponLimit;

const couponIncrease = 10;
const pageCount = Math.ceil(couponLimit / couponIncrease);

let index = 0;

const visualizeCoupon = (index, collection) => {
    const coupon = document.createElement('div');
    coupon.className = 'coupon-unit';

    const imgDiv = document.createElement('div');
    imgDiv.className = 'coupon-image-placeholder';
    imgDiv.innerHTML = `<img class ='coupon' src='../static/pictures/${collection[index].chooseFile.name}'>`;

    let expiresIn = Math.floor((new Date(collection[index].validToDate) - new Date()) / (1000 * 60 * 60 * 24));

    coupon.appendChild(imgDiv);
    const coupInfo = document.createElement('div');
    coupInfo.className = 'coupon-info';
    coupInfo.innerHTML =
        `<div class="coupon-info-parts">
            <span>${collection[index].itemName}</span>
            <a href="#add_to_favourite"><img src="../static/icons/favorite_FILL0_wght400_GRAD0_opsz24.svg"
         alt="favourite"></a>
        </div>
            <div class="coupon-info-parts">
                <span class="brief-desc">${collection[index].shortDescription}</span>
                <label>Expires in ${expiresIn} days</label>
            </div>
                <hr>
    <div class="coupon-info-parts">
        <span>$${collection[index].price}</span>
        <button type="button" class="add-item-button">Add to Cart</button>
    </div>`
    coupon.appendChild(coupInfo);
    couponGrid.appendChild(coupon);
}

const addCoupons = (collection) => {
    for (let i = index, end = index + couponIncrease; i < end && i < collection.length; i++) {
        visualizeCoupon(index, collection);
        index++;
    }
    couponCount.innerHTML = `${index}`
}

window.onload = function () {
    let val = localStorage.getItem('lastScrollPosition');
    let position = Number.parseInt(val);
    while (document.documentElement.scrollHeight < position) {
        addCoupons(allCollection);
    }
    addCoupons(allCollection);
    loadPosition();
}


const handleInfiniteScroll = () => {
    if (window.innerHeight + window.scrollY >= (document.body.offsetHeight * 0.8)) {
        addCoupons(allCollection);
    }
    if (index == couponLimit || index == allCollection.length) {
        loader.style.display = 'none';
    }
}

const func = handleInfiniteScroll.bind(this);



//DEBAUNCE IMPLEMENTATION
const debaunce = (func, threshold, immediately) => {
    let timeout;
    return function debaunced() {
        var context = this, args = arguments;

        function delayed() {
            if (!immediately) {
                func.apply(context, args);
            }
            timeout = null;
        }

        if (timeout) {
            clearTimeout(timeout);
        }
        else if (immediately) {
            func.apply(context, args);
        }
        timeout = setTimeout(delayed, threshold | 500);
    };
}


//THROTTLE IMPLEMENTATION
const throttle = (func, threshold) => {
    let timeout;
    return function () {
        let context = this, args = arguments;

        function later() {
            timeout = false;
        }

        if (!timeout) {
            func.apply(context, args);
            timeout = true;
            setTimeout(later, threshold);
        }
    }
}


//window.addEventListener('scroll', debaunce(func, 500, false));
window.addEventListener('scroll', throttle(func, 500, false));





// //SEARCH IMPLEMENTATION  
// searchField.addEventListener('input', (e) => {
//     let text = document.getElementById('search-field').value.toLowerCase();
//     let filteredCollection = collection.filter((coupon) => {
//         if (coupon.itemName.toLowerCase().includes(text)
//             ||
//             coupon.shortDescription.toLowerCase().includes(text)) {
//             return true;
//         }
//         for (let i = 0; i < coupon.tags.length; i++) {
//             if (coupon.tags[i].toLowerCase().includes(text)) {
//                 return true;
//             }
//         }
//     })
//     index = 0;
//     couponGrid.innerHTML = '';
//     visualizeCoupon(index, filteredCollection);
// })

//Add "to Top" button;
const btn = document.getElementById('upBtn');
btn.addEventListener('click', goUp);
document.addEventListener('scroll', onScroll);
document.addEventListener('scroll', debaunce(savePosition, 500));

function savePosition() {
    localStorage.setItem('lastScrollPosition', window.scrollY);
}

function onScroll() {
    if (window.scrollY >= 50) {
        btn.style.display = 'block';
    }
    else {
        btn.style.display = 'none';
    }
}

function goUp() {
    document.documentElement.scrollTop = 0;
}

//Find last scroll position on the page;

function loadPosition() {
    let val = localStorage.getItem('lastScrollPosition');
    let position = Number.parseInt(val);
    document.documentElement.scrollTop = position;
    console.log(position);
    console.log(document.documentElement.scrollTop)
}

function visualizeCategories() {
    const categoryCatalog = document.querySelector('.category-catalog');
    const searchField = document.getElementById('search-field');
    const select = document.getElementById('tags');
    for (let i = 0; i < categories.length; i++) {
        let innerHtmlCategory = `
                <div class="image-placeholder"></div>
                <h4>${categories[i]}</h4>
        `;
        let tmp = document.createElement('div');
        tmp.classList.add('category-unit');
        tmp.setAttribute('id', categories[i]);
        tmp.innerHTML = innerHtmlCategory;
        categoryCatalog.appendChild(tmp);

        let option = document.createElement('option');
        option.text = categories[i];
        option.value = option.text;
        select.appendChild(option);

        tmp.addEventListener('click', (e) => {
            searchField.value = e.target.closest('.category-unit').id;

            const inputEvent = new Event('input', {
                bubbles: true,
                cancelable: true,
            });
            searchField.dispatchEvent(inputEvent);
        })

        select.addEventListener('click', () => {
            select.addEventListener('change', (e) => {
                let searchKey = e.target.value;
                searchField.value = (e.target.value == 'All Categories') ? '' : searchKey;

                const inputEvent = new Event('input', {
                    bubbles: true,
                    cancelable: true,
                });
                searchField.dispatchEvent(inputEvent);
            })
        })
    }
}