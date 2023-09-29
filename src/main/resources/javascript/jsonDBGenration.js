const fileNames = [
    "pexels-cottonbro-studio-9818645.jpg",
    "pexels-dids-3799324.jpg",
    "pexels-dids-3844788.jpg",
    "pexels-erik-mclean-6654177.jpg",
    "pexels-erik-mclean-7524996.jpg",
    "pexels-jj-jordan-9304725.jpg",
    "pexels-karolina-grabowska-4022082.jpg",
    "pexels-karolina-grabowska-4033323.jpg",
    "pexels-karolina-grabowska-4041268.jpg",
    "pexels-karolina-grabowska-4226881.jpg",
    "pexels-karolina-grabowska-4386402.jpg",
    "pexels-karolina-grabowska-4386404.jpg",
    "pexels-karolina-grabowska-6956661.jpg",
    "pexels-monstera-production-7794266.jpg",
    "pexels-murillo-molissani-10854279.jpg",
    "pexels-steve-johnson-1000366.jpg"
  ];
  
  const possibleTags = [
    "tag1",
    "tag2",
    "tag3",
    "tag4",
    "tag5",
    "tag6",
    "tag7"
  ];
  
  // Generate 90 unique JSON objects with random values
  const generatedJSONs = [];
  
  const futureDates = generateFutureDates(90); // Generate future dates
  
  for (let i = 0; i < 90; i++) {
    const randomIndex = Math.floor(Math.random() * fileNames.length);
    const randomFileName = fileNames[randomIndex];
  
    // Generate between 1 and 3 random tags from possibleTags
    const numTags = Math.floor(Math.random() * 3) + 1; // Random between 1 and 3
    const randomTags = shuffleArray(possibleTags).slice(0, numTags);
    const oneYearAgo = new Date();
  oneYearAgo.setFullYear(oneYearAgo.getFullYear() - 1);
    const randomCreationDate = new Date(
      oneYearAgo.getTime() + Math.random() * (365 * 24 * 60 * 60 * 1000)
    );
  
    const json = {
      "companyName": `Company ${i + 1}`,
      "itemName": `Item ${i + 1}`,
      "validToDate": futureDates[i],
      "shortDescription": `Short description for Item ${i + 1}`,
      "price": (Math.random() * 100).toFixed(2), // Generates a random price
      "chooseFile": {
        "name": randomFileName
      },
      "long-description": `Long description for Item ${i + 1}`,
      "tags": randomTags,
      "creationDate": randomCreationDate.toISOString().split('T')[0] // Format as YYYY-MM-DD
    };
  
    generatedJSONs.push(json);
  }
  
  const fs = require('fs');
  const path = require('path');
  
  // ... (Rest of the code for generating JSON objects)
  
  // Convert the generated JSON array to a string
  const jsonString = JSON.stringify(generatedJSONs, null, 2);

    const outputPath = '/Users/kroshkamedved/IdeaProjects/esm/src/main/resources/static'
  
  // Specify the file path where you want to save the JSON data
  const filePath = path.join(outputPath, 'output.json'); // Adjust the file name and path as needed
  
  // Write the JSON data to the file
  fs.writeFileSync(filePath, jsonString, 'utf8');
  
  console.log('JSON data saved to', filePath);


  
  // Function to generate future dates with some similarities
  function generateFutureDates(count) {
    const currentDate = new Date();
    const dates = [];
  
    for (let i = 0; i < count; i++) {
      const randomOffset = Math.floor(Math.random() * 30); // Random days between 0 and 29
      const futureDate = new Date(currentDate);
      futureDate.setDate(currentDate.getDate() + randomOffset);
  
      dates.push(futureDate.toISOString().split('T')[0]);
    }
  
    return dates;
  }
  
  // Function to shuffle an array
  function shuffleArray(array) {
    const shuffled = array.slice();
    for (let i = shuffled.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
    }
    return shuffled;
  }
  