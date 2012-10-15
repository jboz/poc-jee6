/**
 * Espace the JSF id to be used by JQuery.
 * 
 * @param element id
 * @returns
 */
function escapeId(id) {
	return id.replace(/:/g, "\\:");
}