const button = document.getElementById("captureBtn");
const status = document.getElementById("status");

button.addEventListener("click", () => {
  status.textContent = "Button clicked! It works.";
  console.log("Capture button was clicked");
});