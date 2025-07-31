document.addEventListener("DOMContentLoaded", () => {
  // Makes the close button of (error) notifications work.
  document
    .querySelectorAll(".bx--inline-notification__close-button")
    .forEach((btn) =>
      btn.addEventListener("click", (e) =>
        e.target
          .closest(".bx--inline-notification")
          .setAttribute("style", "display: none")
      )
    );

  const usernameTab = document.getElementById("username-tab");
  const passwordTab = document.getElementById("password-tab");
  const usernameInput = document.getElementById("username");
  const passwordInput = document.getElementById("password");
  const loginForm = document.getElementById("kc-form-login");
  const passwordToggleButton = document.getElementById("password-toggle");

  const Tabs = {
    username: "username",
    password: "password",
  };

  let username = "";
  let password = "";
  let isUsernameInvalid = false;
  let isPasswordInvalid = false;
  let currentTab = Tabs.username;
  let showPassword = false;

  passwordToggleButton.addEventListener("click", () => {
    showPassword = !showPassword;
    if (showPassword) {
      passwordInput.setAttribute("type", "text");
      passwordToggleButton.innerHTML =
        '<svg focusable="false" preserveAspectRatio="xMidYMid meet" xmlns="http://www.w3.org/2000/svg" fill="currentColor" width="16" height="16" viewBox="0 0 16 16" aria-hidden="true" class="icon-visibility-on"><path d="M2.6,11.3l0.7-0.7C2.6,9.8,1.9,9,1.5,8c1-2.5,3.8-4.5,6.5-4.5c0.7,0,1.4,0.1,2,0.4l0.8-0.8C9.9,2.7,9,2.5,8,2.5 C4.7,2.6,1.7,4.7,0.5,7.8c0,0.1,0,0.2,0,0.3C1,9.3,1.7,10.4,2.6,11.3z"></path><path d="M6 7.9c.1-1 .9-1.8 1.8-1.8l.9-.9C7.2 4.7 5.5 5.6 5.1 7.2 5 7.7 5 8.3 5.1 8.8L6 7.9zM15.5 7.8c-.6-1.5-1.6-2.8-2.9-3.7L15 1.7 14.3 1 1 14.3 1.7 15l2.6-2.6c1.1.7 2.4 1 3.7 1.1 3.3-.1 6.3-2.2 7.5-5.3C15.5 8.1 15.5 7.9 15.5 7.8zM10 8c0 1.1-.9 2-2 2-.3 0-.7-.1-1-.3L9.7 7C9.9 7.3 10 7.6 10 8zM8 12.5c-1 0-2.1-.3-3-.8l1.3-1.3c1.4.9 3.2.6 4.2-.8.7-1 .7-2.4 0-3.4l1.4-1.4c1.1.8 2 1.9 2.6 3.2C13.4 10.5 10.6 12.5 8 12.5z"></path></svg>';
    } else {
      passwordInput.setAttribute("type", "password");
      passwordToggleButton.innerHTML =
        '<svg focusable="false" preserveAspectRatio="xMidYMid meet" xmlns="http://www.w3.org/2000/svg" fill="currentColor" width="16" height="16" viewBox="0 0 16 16" aria-hidden="true" class="icon-visibility-on"><path d="M15.5,7.8C14.3,4.7,11.3,2.6,8,2.5C4.7,2.6,1.7,4.7,0.5,7.8c0,0.1,0,0.2,0,0.3c1.2,3.1,4.1,5.2,7.5,5.3 c3.3-0.1,6.3-2.2,7.5-5.3C15.5,8.1,15.5,7.9,15.5,7.8z M8,12.5c-2.7,0-5.4-2-6.5-4.5c1-2.5,3.8-4.5,6.5-4.5s5.4,2,6.5,4.5 C13.4,10.5,10.6,12.5,8,12.5z"></path><path d="M8,5C6.3,5,5,6.3,5,8s1.3,3,3,3s3-1.3,3-3S9.7,5,8,5z M8,10c-1.1,0-2-0.9-2-2s0.9-2,2-2s2,0.9,2,2S9.1,10,8,10z"></path></svg>';
    }
  });

  /** Updates the login UI to reflect the internal state. */
  function syncUiWithState() {
    usernameInput.value = username;
    passwordInput.value = password;
    usernameInput.classList.toggle("invalid", isUsernameInvalid);
    passwordInput.classList.toggle("invalid", isPasswordInvalid);
    usernameTab.classList.toggle("hidden", currentTab !== Tabs.username);
    passwordTab.classList.toggle("hidden", currentTab !== Tabs.password);
  }

  /**
   * Called when the form is submitted.
   * This returns whether the form submission should be prevented
   * or not (i.e. whether the form data should be sent to the server).
   *
   * The latter should only happen when both the username and password are
   * valid for submission (i.e. non-empty).
   */
  function handleFormSubmit() {
    if (currentTab === Tabs.username) {
      username = usernameInput.value;
      isUsernameInvalid = !username;

      if (!isUsernameInvalid) {
        currentTab = Tabs.password;
      }

      // In the username step, we always want to prevent the form
      // from being submitted because the user still has to enter
      // the password.
      syncUiWithState();
      passwordInput.focus();
      return true;
    } else if (currentTab === Tabs.password) {
      password = passwordInput.value;
      isPasswordInvalid = !password;

      // In the password step we only want to prevent the form
      // from being submitted while the password is invalid.
      syncUiWithState();
      return isPasswordInvalid;
    } else {
      console.error(
        `Arrived at an invalid tab: "${currentTab}". ` +
          `This is most likely an error in the code and should be fixed. ` +
          `Resetting to the initial tab.`
      );

      currentTab = Tabs.username;
      syncUiWithState();
      return true;
    }
  }

  loginForm.onsubmit = (e) => {
    const shouldPreventFormSubmit = handleFormSubmit();
    if (shouldPreventFormSubmit) {
      e.preventDefault();
      return false;
    } else {
      // This has been moved out of the KC ftl template.
      // It is, by default, present in the following form:
      //   <form onsubmit="login.disabled = true; return true;" action="${url.loginAction}" method="post" ...>
      // It is moved here, because we require special submission
      // handling due to the form having 2 steps.
      login.disabled = true;
      return true;
    }
  };
});
