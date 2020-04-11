<#include "../header.ftl">

<div class="col-md-12 mt-1">
  <a href="addOrg">Add Org</a>
</div>

<br />
<table>
<tr><th>Login</th><th>Environment</th><th>Description</th><th>Used On</th></tr>
<#list elems as elem>
<tr><td>${elem.login}</td><td>${elem.environment}</td><td>${elem.description}</td><td>${elem.usedOn}</td></tr>
</#list>
</table>

<#include "../footer.ftl">
