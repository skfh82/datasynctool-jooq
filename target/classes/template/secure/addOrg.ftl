<#include "../header.ftl">

<div class="col-md-12 mt-1">
  <form action="/secure/auth/save" method="post">
    <div style="width: 5em; display: inline-block">Name:</div><input type="text" id="siteName" name="siteName"></input><br /><br />
    <div style="margin-left: 5em"><button type="submit">Authorize</button></div>
  </form>
  <br /><br />
  <!--
  <a href="https://login.salesforce.com/services/oauth2/authorize?state=NTEN_Prod&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Faddsite&response_type=code&client_id=3MVG9CEn_O3jvv0wT5QzJ74h7.LbEhNXqCjCIDVHsUSR4Hqjyhf4wWKMPrD3uFTKRb36c0iLXPXHM7Aj1IXJH&prompt=login">Authorize</a>
  -->
</div>

${user!}

<#include "../footer.ftl">
