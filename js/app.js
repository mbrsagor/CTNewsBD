function addTodo() {
    let nameValue = document.querySelector(".input-name").value;
    let description = document.querySelector(".input-description").value;
    let resultsBlock = document.querySelector(".results");
    let resultName = document.querySelector(".name-result");
    let resultDescription = document.querySelector(".title-result");

    resultName.innerHTML += nameValue;
    resultDescription.innerHTML += description;
    resultsBlock.classList.remove("hidden");
}

document.querySelector(".add-todo").addEventListener('click', function (e) {
    addTodo();
    e.preventDefault();
    document.getElementById("todo_form").reset();
});
