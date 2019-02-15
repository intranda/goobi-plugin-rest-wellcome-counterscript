package de.intranda.counterscript.api;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import de.intranda.counterscript.model.MetadataInformation;
import de.intranda.counterscript.persistence.MetadataInformationManager;
import lombok.extern.log4j.Log4j;

//http://localhost:8080/Goobi/api/counterscript/csv
@Log4j
@Path("/counterscript/csv")
public class CsvResource {

    @Context
    UriInfo uriInfo;

    @GET
    @Produces("text/csv")
    public List<MetadataInformation> getCsv() {

        List<MetadataInformation> mdl = null;
        try {
            mdl = MetadataInformationManager.calculateDataDate(null, null, false);
        } catch (SQLException e) {
            log.error(e);
        }

        return mdl;
    }

    @Path("/{startdate}/{enddate}")
    @GET
    @Produces("text/csv")
    public List<MetadataInformation> getCsv(@PathParam("startdate") String start, @PathParam("enddate") String end) {

        List<MetadataInformation> mdl = null;
        try {
            mdl = MetadataInformationManager.calculateDataString(start, end, false);
        } catch (SQLException e) {
            log.error(e);
        }

        return mdl;
    }

    @Path("/withinactive")
    @GET
    @Produces("text/csv")
    public List<MetadataInformation> getCsvWithInactive() {

        List<MetadataInformation> mdl = null;
        try {
            mdl = MetadataInformationManager.calculateDataDate(null, null, true);
        } catch (SQLException e) {
            log.error(e);
        }

        return mdl;
    }

    @Path("/withinactive/{startdate}/{enddate}")
    @GET
    @Produces("text/csv")
    public List<MetadataInformation> getCsvWithInactive(@PathParam("startdate") String start, @PathParam("enddate") String end) {

        List<MetadataInformation> mdl = null;
        try {
            mdl = MetadataInformationManager.calculateDataString(start, end, true);
        } catch (SQLException e) {
            log.error(e);
        }

        return mdl;
    }
}
