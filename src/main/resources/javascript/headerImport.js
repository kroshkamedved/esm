document.addEventListener("DOMContentLoaded", () => {

    const nav = document.querySelector('#header-for-all')
    fetch('../templates/header.html')
        .then(res => res.text())
        .then(data => {
            nav.innerHTML = data;
        }).then(() => {
        var menuTrigger = document.getElementById('menu-checkbox');
        const menuList = document.getElementById('menu-list');

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