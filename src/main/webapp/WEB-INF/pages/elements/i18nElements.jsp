<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<script type="text/javascript">
  var typeOfViewNames = {
    "space": "<s:message code="view.space"/>",
    "myspaceView": "<s:message code="view.type.my.space"/>",
    "sharedView": "<s:message code="view.type.shared.for.me"/>",
    "starredView": "<s:message code="view.type.starred"/>",
    "binView": "<s:message code="view.type.bin"/>"
  }

  var shareText = {
    "showUsersWithAccess": "<s:message code="modal.show.list.href"/>",
    "hideUsersWithAccess": "<s:message code="modal.hide.list.href"/>",
    "noShareUsers": "<s:message code="modal.share.no.users"/>"
  }
</script>
