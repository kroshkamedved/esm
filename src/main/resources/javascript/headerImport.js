document.addEventListener("DOMContentLoaded", () => {

    const nav = document.querySelector('#header-for-all')
    fetch('../templates/header.html')
        .then(res => res.text())
        .then(data => {
            nav.innerHTML = data;
        }).then(() => {
            var menuTrigger = document.getElementById('menu-checkbox');
            const menuList = document.getElementById('menu-list');
            const searchField = document.getElementById('search-field');

            searchField.addEventListener('input', debaunce(addSearch,500))


            document.addEventListener('click', (event) => {
                menuList.style.display = 'flex';
                menuList.style.flexDirection = "column";

                // Check if the click target is outside the menuList
                if (!menuList.contains(event.target) && !menuTrigger.checked) {
                    menuList.style.display = 'none';
                }
            });
        })
})
//Search implementation
function addSearch (e) {
    let text = document.getElementById('search-field').value.toLowerCase();
    //min relevant value for search execution
    if(text.length >= 2){
        let filteredCollection = finalCollection.filter((coupon) => {
            if (coupon.itemName.toLowerCase().includes(text)
                ||
                coupon.shortDescription.toLowerCase().includes(text)) {
                return true;
            }
            for (let i = 0; i < coupon.tags.length; i++) {
                if (coupon.tags[i].toLowerCase().includes(text)) {
                    return true;
                }
            }
        })
        allCollection = filteredCollection;
    }
    index = 0;  
    couponGrid.innerHTML = '';
    if(text==''){
        allCollection = finalCollection;
    }
    addCoupons(allCollection);
}