//Elemente aus Html catchen und in verarbeitbare Konstanten casten
document.addEventListener("DOMContentLoaded", () => {
  const taskInput = document.getElementById("taskInput");
  const addTaskButton = document.getElementById("addTaskButton");
  const openTaskList = document.getElementById("openTaskList");
  const doneTaskList = document.getElementById("doneTaskList");

  // Neue Aufgabe erstellen
  addTaskButton.addEventListener("click", () => {
    const desc = taskInput.value.trim(); //Leerzeiche vor und dahinter entfernen
    if (!desc) return; //falls nichts drinnen steht -> nichts tun
    fetch("/tasks", {
      //Ruft die Browser-Fetch-API auf und sendet eine HTTP-Anfrage an den Backend-Endpunkt
      method: "POST", //Legt fest, dass es eine POST-Anfrage ist – also: „lege etwas Neues an“
      headers: { "Content-Type": "application/json" }, //Teilt dem Server mit, dass der Request-Body im JSON-Format vorliegt.
      body: JSON.stringify({ description: desc }), //konvertiert das JavaScript-Objekt { description: desc } in einen JSON-String
    })
      .then(() => {
        //fetch(...) gibt ein Promise zurück, das erfüllt wird, sobald der Server geantwortet hat.
        //in .then(() => { … }) landen die Befehle, die nach erfolgreichem Absenden und einer erfolgreichen HTTP-Antwort ausgeführt werden.
        taskInput.value = ""; //taskInput.value = "" leert das Eingabefeld, damit der Nutzer sofort eine neue Aufgabe eintippen kann.
        loadTasks(); //loadTasks() ruft deine Funktion zum Nachladen aller Tasks auf, sodass die gerade neu angelegte Aufgabe sofort in der Liste erscheint.
      })
      .catch(console.error);
  });

  // Laden aller Aufgaben
  function loadTasks() {
    fetch("/tasks")
      .then((res) => res.json())
      .then((tasks) => {
        openTaskList.innerHTML = "";
        doneTaskList.innerHTML = "";
        tasks.forEach(renderTask); //verkürzte For Schleife führt in jeden Task die renderTask Funktion aus
      })
      .catch(console.error);
  }

  // Einzelnen Task rendern
  function renderTask(task) {
    const li = document.createElement("li");

    // 1) Checkbox
    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.checked = task.done;
    checkbox.style.marginRight = "0.5rem";

    // Wenn Checkbox angeklickt wird, toggeln wir done
    checkbox.addEventListener("change", () => {
      fetch(`/tasks/${task.id}`, {
        method: "PATCH",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ done: checkbox.checked }),
      })
        .then(() => loadTasks())
        .catch(console.error);
    });

    li.appendChild(checkbox);

    // 2) Beschreibung
    const span = document.createElement("span");
    span.textContent = task.description ?? task.taskDescription ?? "";
    li.appendChild(span);

    // 3) In die richtige Liste einhängen
    if (task.done) {
      doneTaskList.appendChild(li);
    } else {
      openTaskList.appendChild(li);
    }
  }

  // Initialer Aufruf
  loadTasks();
});
