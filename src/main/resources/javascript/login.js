document.addEventListener("DOMContentLoaded", () => {
    var menuTrigger = document.getElementById('menu-checkbox');
    const menuList = document.getElementById('menu-list');

    document.addEventListener('click', (event) => {
            menuList.style.display = 'flex';
            menuList.style.flexDirection = "column";

            // Check if the click target is outside the menuList
            if (!menuList.contains(event.target) && !menuTrigger.checked) {
                menuList.style.display = 'none';
            }
        }
    )
})




