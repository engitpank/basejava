function validateForm(){
    let fullName = document.getElementById("fullName");
    fullName.value = fullName.value.trim();
    if(fullName.value === "") {
        alert("Поле с именем должно содержать более одного символа");
        return false;
    }
    return true;
}