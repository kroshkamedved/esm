const container = document.querySelector('.coupon-unit');

function loadImages(numImages = 10){
    let i = 0;
    while(i < numImages){
        const img = document.createElement('img');
        img.src = `${img_url}`
        const imgContainer = container.querySelector('coupon-image-placeholder');
        imgContainer.appendChild(img);
        i++;
    }
}