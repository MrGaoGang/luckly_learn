var express = require("express");
var router = express.Router();

/* GET home page. */
router.get("/", function(req, res, next) {
  res.render("index", { title: "Express" });
});
router.get("/modules", function(req, res, next) {
  res.json({
    data: [
      {
        name: "module_1"
      },
      {
        name: "module_2"
      }
    ]
  });
});

router.get("/download/:name", function(req, res, next) {
  const name = req.params.name;
  res.download(`static/${name}.zip`);
});

module.exports = router;
