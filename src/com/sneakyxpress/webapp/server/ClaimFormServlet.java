package com.sneakyxpress.webapp.server;

import java.io.IOException;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.sneakyxpress.webapp.shared.TruckClaim;

public class ClaimFormServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		ServletFileUpload upload = new ServletFileUpload();
		TruckClaim truckClaim = new TruckClaim();

		String fileName = "";

		try
		{
			response.setContentType("text/html");
			FileItemIterator iter = upload.getItemIterator(request);

			if (!iter.hasNext())
			{
				response.getWriter().write("Empty form submitted");
				return;
			}

			while (iter.hasNext())
			{

				FileItemStream item = iter.next();

				if (item.isFormField())
				{
					if (item.getFieldName().equals("fbId"))
					{
						truckClaim.setFacebookId(Streams.asString(item
								.openStream()));
					}
					else if (item.getFieldName().equals("vendorId"))
					{
						truckClaim.setTruckId(Streams.asString(item
								.openStream()));
					}
					else if (item.getFieldName().equals("nameBoxInput"))
					{
						truckClaim.setName(Streams.asString(item.openStream()));
					}
					else if (item.getFieldName().equals("emailBoxInput"))
					{
						truckClaim
								.setEmail(Streams.asString(item.openStream()));
					}
					else if (item.getFieldName().equals("phoneBoxInput"))
					{
						truckClaim.setPhoneNumber(Streams.asString(item
								.openStream()));
					}
					else if (item.getFieldName().equals("phoneBoxInput"))
					{
						truckClaim.setPhoneNumber(Streams.asString(item
								.openStream()));
					}
					else if (item.getFieldName().equals("fileNameInput"))
					{
						fileName = Streams.asString(item.openStream());
					}

				}
				else
				{

					System.out.println("This is reached");
					
					blobMess(request, response);
					
					/*String bucket = "sneaky-xpress";

					// Create a BlobKey for a Google Storage File.
					String gs_blob_key = "/gs/" + bucket + "/" + fileName;

					// filename The Google Storage filename. The filename must
					// be in the
					// * format "/gs/bucket_name/object_name".
					BlobKey blob_key = BlobstoreServiceFactory
							.getBlobstoreService().createGsBlobKey(gs_blob_key);

					ServingUrlOptions serving_options = ServingUrlOptions.Builder
							.withBlobKey(blob_key);
					String serving_url = ImagesServiceFactory
							.getImagesService().getServingUrl(serving_options);
					System.out.println("Serving URL: " + serving_url);
					response.getWriter().println(serving_url);*/
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		
		persistClaimData(truckClaim);
		
		response.getWriter().write("Submitted");
		response.getWriter().close();
	}

	public void persistClaimData(TruckClaim claim)
	{
		// Persist truck claim data
		PersistenceManager pm = PMF.get().getPersistenceManager();

		pm.makePersistent(claim);

		pm.close();
	}
	
	public void blobMess(HttpServletRequest request, HttpServletResponse response){
		BlobstoreService blobService = BlobstoreServiceFactory.getBlobstoreService();
		Map<String, BlobKey> blobMap = blobService.getUploadedBlobs(request);
		BlobKey blobKey = blobMap.get("uploadClaimFormElement");
		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		String imageURL = imagesService.getServingUrl(blobKey);
		try
		{
			response.sendRedirect("/sneaky_xpress/claimFormReq"+imageURL);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse res){
		String imageUrl = req.getParameter("imgURL");
		res.setHeader("Content-Type", "text/html");
		try
		{
			res.getWriter().println(imageUrl);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}