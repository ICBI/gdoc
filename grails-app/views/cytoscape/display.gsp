${request.getRequestURL().toString()} <br />
${request.getContextPath()} <br />
${request.getServerPort()} <br />
${request.getRequestURL().toString().split(request.getContextPath())[0]}