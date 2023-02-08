package org.acme;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/roles-allowed-user")
@RolesAllowed("user")
public class RolesAllowedResource {

    @GET
    public void rolesAllowedUser() {
    }

    @GET
    @Path("/roles-allowed-admin")
    @RolesAllowed("admin")
    public void rolesAllowedAdmin() {
    }
}
