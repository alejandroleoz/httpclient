using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Http.Headers;
using System.Net.Http;

namespace Rest
{
    class Client
    {
        private const int REQUEST_TIMEOUT = 5;
        static void Main(string[] args)
        {
            Client c = new Client();
            //Console.WriteLine(c.PostRequest());
            Console.WriteLine(c.GetRequest());
        }

        public string GetRequest()
        {
            string responseString = "";
            HttpClient client = new HttpClient();
            client.Timeout = TimeSpan.FromSeconds(REQUEST_TIMEOUT);

            var response = client.GetAsync("https://ddsfakerestapi.herokuapp.com/Persona/").Result;
            if (response.IsSuccessStatusCode)
            {
                var responseContent = response.Content;
                responseString = responseContent.ReadAsStringAsync().Result;
            }
            client.Dispose();

            return responseString;
        }

        public string PostRequest()
        {
            string responseString = "";
            HttpClient client = new HttpClient();
            client.Timeout = TimeSpan.FromSeconds(REQUEST_TIMEOUT);

            string json = "{ \"dni\": 987, \"nombre\": \"Gustavo\", \"apellido\": \"Silva\", \"edad\": 12 }";
            HttpContent payload = new StringContent(json, Encoding.UTF8, "application/json");
            var response = client.PostAsync("https://ddsfakerestapi.herokuapp.com/Persona/", payload).Result;

            if (response.IsSuccessStatusCode)
            {
                var responseContent = response.Content;
                responseString = responseContent.ReadAsStringAsync().Result;
            }
            client.Dispose();

            return responseString;
        }


    }
}
