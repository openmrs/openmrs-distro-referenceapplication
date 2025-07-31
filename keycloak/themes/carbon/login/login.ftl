<#import "template.ftl" as layout>
<@layout.registrationLayout displayInfo=social.displayInfo displayWide=(realm.password && social.providers??); section>
  <div class="card login-card">
    <div id="kc-locale-div">
      <#if realm.internationalizationEnabled  && locale.supported?size gt 1>
        <div class="${properties.kcLocaleMainClass!}" id="kc-locale">
          <div id="kc-locale-wrapper" class="${properties.kcLocaleWrapperClass!}">
            <div id="kc-locale-dropdown" class="${properties.kcLocaleDropDownClass!}">
              <a href="#" id="kc-current-locale-link">${locale.current}</a>
              <ul id="dropdown-content" class="${properties.kcLocaleListClass!}">
                <#list locale.supported as l>
                  <li class="${properties.kcLocaleListItemClass!}">
                    <a class="${properties.kcLocaleItemClass!}" href="${l.url}">${l.label}</a>
                  </li>
                </#list>
              </ul>
            </div>
          </div>
        </div>
      </#if>
    </div>
    <div class="login-center">
      <img src="${url.resourcesPath}/img/ozone-logo.png" class="center-logo" />
    </div>
    <#if realm.password && social.providers??>class="${properties.kcFormSocialAccountContentClass!}
      ${properties.kcFormSocialAccountClass!}"
    </#if>
    <#if realm.password>
      <form id="kc-form-login" action="${url.loginAction}" method="post">
        <div class="input-group" id="username-tab">
          <div class="form-item">
            <label for="username" class="label">
              <#if !realm.loginWithEmailAllowed>
                ${msg("username")}
              <#elseif !realm.registrationEmailAsUsername>
                ${msg("usernameOrEmail")}
              <#else>
                ${msg("email")}
              </#if>
            </label>
            <div class="text-input-field-outer-wrapper">
              <div class="text-input-field-wrapper">
                <#if usernameEditDisabled??>
                  <input
                    autofocus
                    id="username"
                    class="${properties.kcInputClass!}"
                    name="username"
                    value="${(login.username!'')}"
                    type="text"
                    disabled
                    placeholder="<#if
                    !realm.loginWithEmailAllowed>${msg(" username")}<#elseif
                    !realm.registrationEmailAsUsername>${msg("usernameOrEmail")}<#else
                    >${msg("email")}</#if>"
                  />
                <#else>
                  <input
                    autofocus
                    id="username"
                    class="${properties.kcInputClass!}"
                    name="username"
                    value="${(login.username!'')}"
                    type="text"
                    placeholder="<#if
                    !realm.loginWithEmailAllowed>${msg(" username")}<#elseif
                    !realm.registrationEmailAsUsername>${msg("usernameOrEmail")}<#else
                    >${msg("email")}</#if>"
                    autocomplete="off"
                  />
                </#if>
              </div>
            </div>
          </div>
          <button type="submit" class="continue-button btn-primary">
            ${msg("doContinue")}
            <svg focusable="false" preserveAspectRatio="xMidYMid meet" xmlns="http://www.w3.org/2000/svg" fill="currentColor"
              aria-label="Next" aria-hidden="true" width="24" height="24" viewBox="0 0 24 24" role="img" class="btn-icon">
              <path d="M14 4L12.9 5.1 18.9 11.2 2 11.2 2 12.8 18.9 12.8 12.9 18.9 14 20 22 12z"></path>
            </svg>
          </button>
        </div>

        <div class="input-group hidden" id="password-tab">
          <div class="form-item">
            <label for="password" class="label">${msg("password")}</label>
            <div class="text-input-field-outer-wrapper">
              <div class="text-input-field-wrapper">
                <input
                  id="password"
                  type="password"
                  name="password"
                  class="login-input-style text-input password-input"
                  data-toggle-password-visibility="true" 
                  autofocus
                />
                <button id="password-toggle" type="button" class="password-visibility-toggle">
                  <svg focusable="false" preserveAspectRatio="xMidYMid meet" xmlns="http://www.w3.org/2000/svg" fill="currentColor" width="16" height="16" viewBox="0 0 16 16" aria-hidden="true" class="icon-visibility-on">
                    <path d="M15.5,7.8C14.3,4.7,11.3,2.6,8,2.5C4.7,2.6,1.7,4.7,0.5,7.8c0,0.1,0,0.2,0,0.3c1.2,3.1,4.1,5.2,7.5,5.3 c3.3-0.1,6.3-2.2,7.5-5.3C15.5,8.1,15.5,7.9,15.5,7.8z M8,12.5c-2.7,0-5.4-2-6.5-4.5c1-2.5,3.8-4.5,6.5-4.5s5.4,2,6.5,4.5 C13.4,10.5,10.6,12.5,8,12.5z"></path>
                    <path d="M8,5C6.3,5,5,6.3,5,8s1.3,3,3,3s3-1.3,3-3S9.7,5,8,5z M8,10c-1.1,0-2-0.9-2-2s0.9-2,2-2s2,0.9,2,2S9.1,10,8,10z"></path>
                  </svg>
                </button>
              </div> 
            </div>
          </div>
          <button type="submit" class="continue-button btn-primary">
            ${msg("doLogIn")}
            <svg focusable="false" preserveAspectRatio="xMidYMid meet" xmlns="http://www.w3.org/2000/svg"
              fill="currentColor" aria-label="Next" aria-hidden="true" width="24" height="24" viewBox="0 0 24 24" role="img"
              class="btn-icon">
              <path d="M14 4L12.9 5.1 18.9 11.2 2 11.2 2 12.8 18.9 12.8 12.9 18.9 14 20 22 12z"></path>
            </svg>
          </button>
        </div>
      </form>
    </#if>
  </div>
  <div class="need-help">
    <div class="need-help-item">
      <a href="http://ozone-his.com/" class="support-link">${msg("website")}</a>
    </div>
    <div class="need-help-item">
      <a href="https://talk.openmrs.org/c/software/ozone-his" class="support-link">${msg("forum")}</a>
    </div>
    <div class="need-help-item">
      <a href="https://openmrs.slack.com/archives/C02PYQD5D0A" class="support-link">${msg("slack")}</a>
    </div>
  </div>

  <div class="footer">
    <p class="powered-by-text">${msg("poweredBy")}</p>
    <div class="powered-by-logos">
      <div class="powered-by-logo">
        <img src="${url.resourcesPath}/img/openmrs-logo.svg" width="100%" />
      </div>
      <div class="powered-by-logo" style="margin-top: -0.5rem">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 919 495">
          <path
            d="M695,346a75,75,0,1,1,75-75A75,75,0,0,1,695,346Zm0-31a44,44,0,1,0-44-44A44,44,0,0,0,695,315ZM538,346a75,75,0,1,1,75-75A75,75,0,0,1,538,346Zm0-31a44,44,0,1,0-44-44A44,44,0,0,0,538,315Zm-82-45c0,41.9-33.6,76-75,76s-75-34-75-75.9S336.5,196,381,196c16.4,0,31.6,3.5,44,12.6V165.1c0-8.3,7.3-15.1,15.5-15.1s15.5,6.8,15.5,15.1Zm-75,45a44,44,0,1,0-44-44A44,44,0,0,0,381,315Z"
            style="fill: #8f8f8f" />
          <path d="M224,346a75,75,0,1,1,75-75A75,75,0,0,1,224,346Zm0-31a44,44,0,1,0-44-44A44,44,0,0,0,224,315Z"
            style="fill: #714b67" />
        </svg>
      </div>
      <div class="powered-by-logo">
        <img src="${url.resourcesPath}/img/senaite-small.png" width="100%" />
      </div>
      <div class="powered-by-logo" style="margin-top: -0.2rem">
        <img src="${url.resourcesPath}/img/superset-logo-small.png" width="100%" />
      </div>
    </div>
  </div>
</@layout.registrationLayout>