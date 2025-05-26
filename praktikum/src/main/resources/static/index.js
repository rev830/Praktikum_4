document.addEventListener("DOMContentLoaded", () => {
  const taskInput = document.getElementById("taskInput");
  const addTaskButton = document.getElementById("addTaskButton");
  const openTaskList = document.getElementById("openTaskList");
  const doneTaskList = document.getElementById("doneTaskList");

  // Neue Aufgabe erstellen
  addTaskButton.addEventListener("click", () => {
    const description = taskInput.value.trim();
    if (!description) return;

    fetch("/tasks", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ description }),
    })
      .then((res) => res.json())
      .then((task) => {
        taskInput.value = "";
        addTaskToUI(task);
      })
      .catch((err) => console.error("Fehler beim Erstellen:", err));
  });

  // Aufgaben vom Server laden
  function loadTasks() {
    fetch("/tasks")
      .then((res) => res.json())
      .then((tasks) => {
        console.log(">> Tasks vom Server:", tasks); //Logging fürs debuggen
        openTaskList.innerHTML = "";
        doneTaskList.innerHTML = "";

        tasks.forEach(addTaskToUI);
      });
  }

  // Aufgabe in UI einfügen
  function addTaskToUI(task) {
    const li = document.createElement("li");

    // Text als separates Element
    const span = document.createElement("span");
    span.textContent = task.taskDescription || task.description;
    li.appendChild(span);

    if (!task.done) {
      const doneBtn = document.createElement("button");
      doneBtn.textContent = "✅ erledigt";
      doneBtn.style.marginLeft = "1rem";
      doneBtn.addEventListener("click", () => {
        fetch(`/tasks/${task.id}`, {
          method: "PATCH",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ done: true }),
        })
          .then((res) => res.json())
          .then(() => {
            loadTasks();
          });
      });
      li.appendChild(doneBtn);
      openTaskList.appendChild(li);
    } else {
      doneTaskList.appendChild(li);
    }
  }

  // Initiales Laden
  loadTasks();
});
