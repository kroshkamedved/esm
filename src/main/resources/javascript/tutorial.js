document.addEventListener("DOMContentLoaded", () => {
    var menuTrigger = document.getElementById('menu-checkbox');
    const menuList = document.getElementById('menu-list');
// TODO checkbox reaction - show list:

    if(menuList.checked()){
                menuList.style.display = 'flex';
                menuList.style.flexDirection = "column";
    }
    document.addEventListener('click', (event) => {
        // Check if the click target is outside the menuList
        if (!menuList.contains(event.target) && event.target !== menuTrigger) {
            menuList.style.display = 'none';
        }
    });
})