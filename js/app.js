function addTodo() {
    var nameValue = document.querySelector(".input-name").value;
    var jobValue = document.querySelector(".input-job").value;
    var resultsBlock = document.querySelector(".results");
    var resultName = document.querySelector(".name-result");
    var resultJob = document.querySelector(".job-result");

    resultName.innerHTML += nameValue;
    resultJob.innerHTML += jobValue;
    resultsBlock.classList.remove("hidden");
}

document.querySelector(".add-todo").addEventListener('click', function () {
    addTodo();
});
