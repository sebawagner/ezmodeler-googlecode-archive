( 
    SELECT 
        SQL_CALC_FOUND_ROWS c.diagramobject_id, c.name, c.objecttypename, c.inserted, c.updated 
    FROM diagramobject c 
    INNER JOIN diagraminstanceobject dioObject ON (dioObject.diagramobject_id = c.diagramobject_id) 
    INNER JOIN diagramsummary diaSummery ON (diaSummery.diagram_id = dioObject.diagram_id) 
    WHERE 
	    c.organisation_id=1 
	    AND lower(c.name) LIKE lower('%') 
	    AND c.deleted NOT LIKE 'true'
    GROUP BY c.diagramobject_id 
) UNION ( 
    SELECT 
        SQL_CALC_FOUND_ROWS c.diagramobject_id, c.name, c.objecttypename, c.inserted, c.updated 
    FROM diagramobject c 
    INNER JOIN datacarrier_diagramobject dCarrier ON (c.diagramobject_id = dCarrier.diagramobject_id) 
    INNER JOIN diagraminstanceobject dioObject ON (dioObject.diagramobject_id = dCarrier.datacarrier_object_diagramobject_id) 
    INNER JOIN diagramsummary diaSummery ON (diaSummery.diagram_id = dioObject.diagram_id)  
    GROUP BY c.diagramobject_id  
)  
ORDER BY c.name DESC 
LIMIT 0, 500