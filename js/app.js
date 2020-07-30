function addTodo() {
    var nameValue = document.querySelector(".input-name").value;
    var description = document.querySelector(".input-description").value;
    var resultsBlock = document.querySelector(".results");
    var resultName = document.querySelector(".name-result");
    var resultDescription = document.querySelector(".title-result");

    resultName.innerHTML += nameValue;
    resultDescription.innerHTML += description;
    resultsBlock.classList.remove("hidden");
}

document.querySelector(".add-todo").addEventListener('click', function (e) {
    addTodo();
    e.preventDefault();
    document.getElementById("todo_form").reset();
});
