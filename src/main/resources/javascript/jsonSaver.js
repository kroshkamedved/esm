const form = document.querySelector('form');
let quantity = localStorage.getItem('certificatesQuantity');
if(quantity == 'NaN' ||  quantity == undefined || quantity == null ){
   quantity = 0;
   localStorage.setItem('certificatesQuantity', quantity)
}

form.addEventListener('submit',(e)=>{
   const formData = new FormData(form);
    // const obj = Object.fromEntries(formData);
    // const jsonObj = JSON.stringify(obj);
    //console.log(formData.getAll('tags'));

    const date = new Date();
    let day = date.getDate();
    let month = date.getMonth()+1;
    let year = date.getFullYear();

    let currentDateStr = `${year}-${month}-${day}`;
    let dataDate = formData.get('valid-to-date');
        const currentDate = new Date(currentDateStr);
        const inputDate = new Date(dataDate);

    if( currentDate > inputDate) {
            alert('choose correct date');
            return false;
    }

    let obj = {};
    let tags = [];
    formData.forEach((value,key) => {
        if(key === 'chooseFile') {
            obj[key] = {
                name : value.name,
                size: value.size,
                type: value.type,
                lastModified: value.lastModified,
                lastModifiedDate: value.lastModifiedDate,
            }
        }
        else if(key === 'tags'){
            tags.push(value);
        }
        else {
            obj[key] = value;
        }
    })
    obj['tags'] = tags;
    
    localStorage.setItem(`certificate${quantity}`,JSON.stringify(obj));
    let currentQuantity  = Number.parseInt(quantity);
    currentQuantity++;
    localStorage.setItem('certificatesQuantity', currentQuantity.toString());
    e.preventDefault();
})
 

const fileSelector = document.getElementById('chooseFile');
fileSelector.addEventListener('change', () => {
    let fileName = fileSelector.value;
    const fileUpload = document.querySelector('.file-upload');
   
    if(/˄s$/.test(fileName)){
        fileUpload.classList.remove('active');
        filePath.textContent = 'No file chosen...';
    }
    else{
        fileUpload.classList.add('active');
        document.getElementById('noFile').textContent = fileName.replace("C:\\fakepath\\", '');
    }
})

const date = new Date();
let day = date.getDate();
let month = date.getMonth();
let year = date.getFullYear();

let currentDate = `${year}-${month}-${day}`;

document.getElementById('chooseJsons').addEventListener('change',(event)=>{
    const file = event.target.files[0];
    if(!file){
        console.error('No file selected');
        return;
    }
    const reader = new FileReader();

    reader.onload = function(e) {
        const fileContent = e.target.result;
    try {
        const jsonArray = JSON.parse(fileContent);
        localStorage.setItem('couponCollection', JSON.stringify(jsonArray));
        console.log('json saved to the local storage')
    } catch (error) {
        console.log('Error parsing JSON');
    }
};
reader.readAsText(file);
})

