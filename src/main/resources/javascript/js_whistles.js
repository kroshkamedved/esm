document.addEventListener("DOMContentLoaded", ()=>{
    const menuTrigger = document.getElementById('menu-trigger');
    const menuList = document.getElementById('menu-list');

    menuTrigger.addEventListener('mouseenter',
        () => menuList.style.display = 'flex');
    menuList.addEventListener('mouseenter',
        () => menuList.style.display = 'flex');
    menuList.addEventListener('mouseleave',
        () => menuList.style.display = 'none');
    document.addEventListener("click",
        () => menuList.style.display = 'none');
})