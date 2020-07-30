// selector
const todoInput = document.querySelector('.todo_input');
const todoButton = document.querySelector('.todo_button');
const todoList = document.querySelector('.todo_list');

// Event Listeners
todoButton.addEventListener('click', addTodo);

// Functions
function addTodo(event) {
    // console.log('Hello Sagor');
    event.preventDefault();
    const todo_div = document.createElement('li');
    todo_div.classList.add('todo_div');

    const newToDo = document.createElement('li');
    newToDo.innerText = 'Hey';
    addTodo.classList.add('todo-item');
    todo_div.appendChild(newToDo);
}

