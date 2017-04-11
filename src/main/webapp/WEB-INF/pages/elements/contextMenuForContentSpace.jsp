<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 05.04.2017
  Time: 22:52
  To change this template use File | Settings | File Templates.
--%>

<ul id="contextMenu" class="dropdown-menu" role="menu" style="display:none" >
    <!--<li><input type="submit" name="delete" value="Delete"/></li>-->
    <li class="contextHref"><a href="#" data-toggle="modal" data-target="#modalForDelete">Delete</a></li>
    <li class="contextHref"><input type="submit" class="contextInput" name="download" value="Download"/></li>
    <li class="divider"></li>
    <li class="contextHref"><input type="submit" class="contextInput" name="starred" value="Starred"/></li>
    <li class="contextHref"><input type="submit" class="contextInput" name="removestar" value="Remove star"/></li>
    <li class="divider"></li>
    <li class="contextHref"><a href="#" data-toggle="modal" data-target="#modalForRename">Rename</a></li>
    <li class="contextHref"><a href="#" data-toggle="modal" data-target="#modalForShare">Share</a></li>
</ul>
