<!DOCTYPE html>
<html>
<head>
  <title>Your JSP Page</title>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body onload="dubCall()">
  <label for="resourceIdInput">Resource ID:</label>
  <input type="number" id="resourceIdInput" name="resourceId" required>
  <button onclick="dubber()">Submit</button>

  <div class="chart-row" id="chartRow1">
    <div class="chart-container">
      <h1>Project Wise Time spent(Hrs)</h1>
      <h1 id="nores"></h1>
      <canvas id="pieChart"></canvas>
    </div>

    <div class="chart-container">
      <h1>Module Wise Time spent(Hrs)</h1>
      <h1 id="noresModule"></h1>
      <canvas id="pieChartModule"></canvas>
    </div>
  </div>

  <div class="chart-row" id="chartRow2">
    <div class="chart-container">
      <h1>Task Wise Time spent(Hrs)</h1>
      <h1 id="noresTask"></h1>
      <canvas id="pieChartTask"></canvas>
    </div>

    <div class="chart-container">
      <h1>SubTask Wise Time spent(Hrs)</h1>
      <h1 id="noresSubTask"></h1>
      <canvas id="pieChartSubTask"></canvas>
    </div>
  </div>

  <script>
  var pieChart = null;
  var pieChartModule = null;
  var pieChartTask = null;
  var pieChartSubTask = null;
  var analyticsData = null;
  var projectSummaries = null;
  var moduleSummaries = null;
  var taskSummaries = null;
  var subtaskSummaries = null;
  
  function dubCall() {
    createChart("pieChart", [100], ['#dddddd']);
    createChart("pieChartModule", [100], ['#dddddd']);
    createChart("pieChartTask", [100], ['#dddddd']);
    createChart("pieChartSubTask", [100], ['#dddddd']);
  }
  function createChart(chartId, data, backgroundColors) {
    var chartCanvas = document.getElementById(chartId);
    var chart;
    if (chartId === "pieChart") {
      chart = pieChart;
    } else if (chartId === "pieChartModule") {
      chart = pieChartModule;
    } else if (chartId === "pieChartTask") {
      chart = pieChartTask;
    } else if (chartId === "pieChartSubTask") {
      chart = pieChartSubTask;
    }
    if (chart) {
      chart.destroy();
    }
    chart = new Chart(chartCanvas, {
      type: 'pie',
      data: {
        labels: ['Select Resource'],
        datasets: [{
          data: data,
          backgroundColor: backgroundColors,
          borderWidth: 0
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        tooltips: {
          enabled: true,
          callbacks: {
            label: function (tooltipItem, data) {
              var label = data.labels[tooltipItem.index];
              var value = data.datasets[0].data[tooltipItem.index];
              return label + ': ' + value + ' Hrs';
            }
          }
        },
        hover: {
          mode: 'nearest',
          intersect: true
        },
        onClick: function (e, elements) {
          if (elements && elements.length > 0) {
        	  console.log("elms",elements);
        	  
            var clickedElement = elements[0];
            /* console.log("clickedElement",clickedElement); */
            
            var label = chart.data.labels[clickedElement.index];
            var value = chart.data.datasets[0].data[clickedElement.index];
            /* console.log("Clicked: label ", label," value ", value); */
            
            var projectId = projectSummaries[clickedElement.index].projId;
            window.location.href = 'projectDetails?projectId='+projectId; 
            
          }
        },
        plugins: {
          legend: {
            display: true
          }
        }
      }
    });
    if (chartId === "pieChart") {
      pieChart = chart;
    } else if (chartId === "pieChartModule") {
      pieChartModule = chart;
    } else if (chartId === "pieChartTask") {
      pieChartTask = chart;
    } else if (chartId === "pieChartSubTask") {
      pieChartSubTask = chart;
    }
  }
  function dubber() {
    var resourceId = $("#resourceIdInput").val();
    $.ajax({
      type: "POST",
      url: "getUserAnalytics",
      data: {
        "user_id": resourceId
      },
      success: function (response) {
        try {
          analyticsData = JSON.parse(response);
          projectSummaries = analyticsData.projectSummaries;
          moduleSummaries = analyticsData.moduleSummaries;
          taskSummaries = analyticsData.taskSummaries;
          subtaskSummaries = analyticsData.subtaskSummaries;
          // Update project pie chart
          var projectLabels = projectSummaries.map(function (summary) {
            return summary.projName;
          });
          var projectData = projectSummaries.map(function (summary) {
            return summary.totalWorkingHours;
          });
          var projectColors = generateRandomColors(projectLabels.length);
          updatePieChart("pieChart", projectLabels, projectData, projectColors);
          // Update module pie chart
          var moduleLabels = moduleSummaries.map(function (summary) {
            return summary.modlName;
          });
          var moduleData = moduleSummaries.map(function (summary) {
            return summary.totalWorkingHours;
          });
          var moduleColors = generateRandomColors(moduleLabels.length);
          updatePieChart("pieChartModule", moduleLabels, moduleData, moduleColors);
          // Update task pie chart
          var taskLabels = taskSummaries.map(function (summary) {
            return summary.taskName;
          });
          var taskData = taskSummaries.map(function (summary) {
            return summary.totalWorkingHours;
          });
          var taskColors = generateRandomColors(taskLabels.length);
          updatePieChart("pieChartTask", taskLabels, taskData, taskColors);
          // Update subtask pie chart
          var subtaskLabels = subtaskSummaries.map(function (summary) {
            return "Subtask " + summary.subtaskId;
          });
          var subtaskData = subtaskSummaries.map(function (summary) {
            return summary.totalWorkingHours;
          });
          var subtaskColors = generateRandomColors(subtaskLabels.length);
          updatePieChart("pieChartSubTask", subtaskLabels, subtaskData, subtaskColors);
          // Rest of the code...
        } catch (error) {
          console.error('Error parsing JSON:', error);
          // Handle the error appropriately
        }
      },
      error: function () {
        document.getElementById("nores").innerHTML = "Error occurred while fetching data";
        document.getElementById("noresModule").innerHTML = "Error occurred while fetching data";
        document.getElementById("noresTask").innerHTML = "Error occurred while fetching data";
        document.getElementById("noresSubTask").innerHTML = "Error occurred while fetching data";
        destroyChart();
      }
    });
  }
  function generateRandomColors(count) {
    var colors = [];
    for (var i = 0; i < count; i++) {
      colors.push(getRandomColor());
    }
    return colors;
  }
  function getRandomColor() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }
  function destroyChart() {
    if (pieChart) {
      pieChart.destroy();
      pieChart = null;
    }
    if (pieChartModule) {
      pieChartModule.destroy();
      pieChartModule = null;
    }
    if (pieChartTask) {
      pieChartTask.destroy();
      pieChartTask = null;
    }
    if (pieChartSubTask) {
      pieChartSubTask.destroy();
      pieChartSubTask = null;
    }
  }

  function updatePieChart(chartId, labels, data, colors) {
    var chart = null;
    if (chartId === "pieChart") {
      chart = pieChart;
    } else if (chartId === "pieChartModule") {
      chart = pieChartModule;
    } else if (chartId === "pieChartTask") {
      chart = pieChartTask;
    } else if (chartId === "pieChartSubTask") {
      chart = pieChartSubTask;
    }
    if (chart) {
      chart.data.labels = labels;
      chart.data.datasets[0].data = data;
      chart.data.datasets[0].backgroundColor = colors;
      chart.update();
    }
  }
  </script>

  <style>
  .chart-row {
    display: flex;
    margin-bottom: 100px;
    margin-top: 20px;
  }
  .chart-container {
    flex: 1;
    width: 100%;
    max-width: 500px;
    height: 600px;
    margin: 10px 10px 10px 10px;
    background-color: #f5f5f5;
    border: 1px solid #ccc;
    padding: 20px;
    border-radius: 5px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1), 0 8px 24px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
  }
  .chart-container canvas {
    width: 100% !important;
    height: auto !important;
    max-height: 400px;
  }
  .chart-container h1 {
    margin-top: 0;
    margin-bottom: 10px;
  }
  </style>
</body>
</html>