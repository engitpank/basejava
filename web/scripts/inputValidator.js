function validateForm() {
    let fullName = document.getElementById("fullName");
    fullName.value = fullName.value.trim();
    if (fullName.value === "") {
        alert("Поле с именем должно содержать более одного символа");
        return false;
    }
    return true;
}

(function () {
    let dateInput = document.getElementsByClassName("date");
    for (let i = 0; i < dateInput.length; i++) {
        dateInput[i].addEventListener('keydown', event => {
            if (event.keyCode === 46 || event.keyCode === 8 || event.keyCode === 9 || event.keyCode === 27 ||
                (event.keyCode === 65 && event.ctrlKey === true) ||
                (event.keyCode >= 35 && event.keyCode <= 39)) {

            } else {
                if (dateInput[i].value.length > 6) {
                    event.preventDefault();
                }
                if ((event.keyCode < 48 || event.keyCode > 57) && (event.keyCode < 96 || event.keyCode > 105)) {
                    event.preventDefault();
                } else {
                    if (dateInput[i].value.length === 4) {
                        dateInput[i].value += "-";
                    }
                }
            }

        });
    }
}());

(function () {
    let dateCheckbox = document.getElementsByClassName("date-checkbox");
    let dateInput = document.getElementsByClassName("finishDate");
    document.addEventListener('DOMContentLoaded', () => {
        for (let i = 0; i < dateCheckbox.length; i++) {
            dateCheckbox[i].addEventListener("change", () => {
                if (dateCheckbox[i].checked) {
                    dateInput[i].value = "";
                    dateInput[i].disabled = true;
                    dateInput[i].style.height = "0px";
                } else {
                    dateInput[i].disabled = false;
                    dateInput[i].style.height = defaultHeight;
                }
            });
        }
    });
    let defaultHeight = dateInput[0].style.height;
    for (let i = 0; i < dateCheckbox.length; i++) {
        if (dateInput[i].value === dateCheckbox[i].value) {
            dateInput[i].value = "";
            dateInput[i].disabled = true;
            dateCheckbox[i].checked = true;
            dateInput[i].style.height = "0px";
            // dateInput[i].setAttribute("type", "hidden");
        } else {
            dateCheckbox[i].checked = false;
            dateInput[i].style.height = defaultHeight;
        }
    }
}());

