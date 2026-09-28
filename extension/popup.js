// Where the backend listens. This exact address is also listed in
// manifest.json under host_permissions, or Chrome would block the request.
const API_URL = "http://localhost:8080/api/v1/applications";

const companyInput = document.getElementById("company");
const roleInput = document.getElementById("role");
const urlInput = document.getElementById("url");
const form = document.getElementById("application-form");
const saveButton = document.getElementById("save-button");
const statusBox = document.getElementById("status");

function setStatus(message, className) {
  statusBox.textContent = message;
  statusBox.className = className || "";
}

// "submit" rather than the button's "click", so pressing Enter in any field
// saves too.
form.addEventListener("submit", async (event) => {
  // Without this the browser does a normal form submit, which reloads the popup
  // and wipes what was typed.
  event.preventDefault();

  // Disabled while the request is in flight, so an impatient double-click can't
  // send the same application twice.
  saveButton.disabled = true;
  saveButton.textContent = "Saving...";
  setStatus("");

  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        company: companyInput.value.trim(),
        role: roleInput.value.trim(),
        url: urlInput.value.trim()
      })
    });

    if (response.ok) {
      setStatus("Saved!", "success");
      form.reset();
    } else {
      // The API always answers errors with {status, message}, so showing
      // message is how "Already applied to this job" reaches the user on a 409.
      const body = await response.json().catch(() => null);
      setStatus(
        body && body.message ? body.message : `Request failed (${response.status})`,
        "error"
      );
    }
  } catch (error) {
    // fetch only throws when no response arrived at all, so this means the
    // backend isn't reachable - not that it returned an error.
    setStatus("Couldn't reach the API. Is the backend running?", "error");
  } finally {
    // finally runs whether the save succeeded or failed, so the button can
    // never be left stuck on "Saving...".
    saveButton.disabled = false;
    saveButton.textContent = "Save";
  }
});
