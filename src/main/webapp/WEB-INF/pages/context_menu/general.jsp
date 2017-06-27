<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 25.04.2017
  Time: 10:42
  To change this template use File | Settings | File Templates.
--%>

<ul id="contextMenu" class="dropdown-menu" role="menu" style="display:none" >
    <li class=""><a class="contextHref" href="#" data-toggle="modal" data-target="#modalForRemove">Remove</a></li>
    <li class="contextHref"><input type="submit" class="contextInput" name="download" value="Download"/></li>
    <li class="divider"></li>
    <li class="contextHref li_starred"><input type="submit" class="contextInput" name="starred" value="Starred"/></li>
    <li class="contextHref li_removestar"><input type="submit" class="contextInput" name="removestar" value="Remove star"/></li>
    <li class="divider"></li>
    <li class="li_rename"><a class="contextHref"  href="#" data-toggle="modal" data-target="#modalForRename">Rename</a></li>
    <li class="li_share"><a class="contextHref"  href="#" data-toggle="modal" data-target="#modalForShare">Share</a></li>
    <li class="divider"></li>
    <li class="contextHref"><input type="submit" class="contextInput" name="addtome" value="Add to my store"/></li>
    <li class="li_replace"><a class="contextHref"  href="#" data-toggle="modal" data-target="#modalForReplace">Replace</a></li>
</ul>

